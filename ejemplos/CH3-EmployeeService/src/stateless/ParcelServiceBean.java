package stateless;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Parcel;

@Stateless
public class ParcelServiceBean {

  /*
   * Instance variables
   */
  @PersistenceContext(unitName = "EmployeeService")
  private EntityManager entityManager;

  public void setEntityManager(EntityManager localEntityManager) {
    entityManager = localEntityManager;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public Parcel create(Parcel newParcel) {
    entityManager.persist(newParcel);
    return newParcel;
  }

  /**
   * Elimina logicamente una parcela de un usuario
   * 
   * @param userId
   * @param parcelId
   * @return referencia a un objeto de tipo Parcel en
   * caso de eliminar una parcela, null en caso contrario
   */
  public Parcel remove(int userId, int parcelId) {
    Parcel givenParcel = find(userId, parcelId);

    if (givenParcel != null) {
      givenParcel.setActive(false);
      return givenParcel;
    }

    return null;
  }

  /**
   * Modifica una parcela de un usuario
   *
   * @param userId
   * @param parcelId
   * @param modifiedParcel
   * @return referencia a un objeto de tipo Parcel si se modifica
   * la parcela con el ID y el ID de usuario provistos, null en
   * caso contrario
   */
  public Parcel modify(int userId, int parcelId, Parcel modifiedParcel) {
    Parcel choosenParcel = find(userId, parcelId);

    if (choosenParcel != null) {
      /*
       * TODO: Leer
       * Probablemente se tenga que hacer una validacion
       * que impida que un mismo usuario cliente cargue
       * para si mismo mas de una parcela con el mismo
       * nombre
       */
      choosenParcel.setName(modifiedParcel.getName());
      choosenParcel.setHectare(modifiedParcel.getHectare());
      choosenParcel.setLongitude(modifiedParcel.getLongitude());
      choosenParcel.setLatitude(modifiedParcel.getLatitude());
      choosenParcel.setRegistrationDate(modifiedParcel.getRegistrationDate());
      choosenParcel.setActive(modifiedParcel.getActive());
      return choosenParcel;
    }

    return null;
  }

  /**
   * Retorna una parcela de un usuario
   * 
   * @param userId
   * @param parcelId
   * @return referencia a un objeto de tipo Parcel perteneciente
   * al usuario con el ID dado
   */
  public Parcel find(int userId, int parcelId) {
    Query query = entityManager.createQuery("SELECT p FROM Parcel p WHERE (p.id = :parcelId AND p.user.id = :userId)");
    query.setParameter("parcelId", parcelId);
    query.setParameter("userId", userId);

    return (Parcel) query.getSingleResult();
  }

  /**
   * Comprueba si una parcela pertenece a un usuario.
   * 
   * Retorna true si y solo si una parcela pertenece a un
   * usuario.
   * 
   * @param userId
   * @param parcelId
   * @return true si se encuentra la parcela con el ID y el
   * ID de usuario provistos, false en caso contrario
   */
  public boolean checkUserOwnership(int userId, int parcelId) {
    boolean result = false;

    try {
      find(userId, parcelId);
      result = true;
    } catch (NoResultException e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Retorna las parcelas de un usuario
   * 
   * @param userId
   * @return referencia a un objeto de tipo Collection que
   * contiene todas las parcelas del usuario con el ID dado
   */
  public Collection<Parcel> findAll(int userId) {
    Query query = entityManager.createQuery("SELECT p FROM Parcel p WHERE p.user.id = :userId ORDER BY p.id");
    query.setParameter("userId", userId);

    return (Collection) query.getResultList();
  }

}

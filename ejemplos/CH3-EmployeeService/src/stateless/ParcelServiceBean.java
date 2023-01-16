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
   * Elimina logicamente una parcela mediante su ID
   *
   * @param  id
   * @return Una referencia de tipo Parcel en caso de eliminar la parcela,
   * en caso contrario retorna null
   */
  public Parcel remove(int id) {
    Parcel parcel = find(id);

    if (parcel != null) {
      parcel.setActive(false);
      return parcel;
    }

    return null;
  }

  /**
   * Actualiza o modifica la entidad asociada al id dado
   *
   * @param  id
   * @param  modifiedParcel
   * @return un valor no nulo en caso de modificar la entidad solicitada
   * mediante el id, en caso contrario retorna un valor nulo
   */
  public Parcel modify(int id, Parcel modifiedParcel) {
    Parcel choosenParcel = find(id);

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

  public Parcel find(int id) {
    return entityManager.find(Parcel.class, id);
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

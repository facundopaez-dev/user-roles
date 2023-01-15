package stateless;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.ClimateRecord;
import model.Parcel;

@Stateless
public class ClimateRecordServiceBean {

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

  public ClimateRecord create(ClimateRecord newClimateRecord) {
    getEntityManager().persist(newClimateRecord);
    return newClimateRecord;
  }

  public ClimateRecord remove(int id) {
    ClimateRecord givenClimateRecord = find(id);

    if (givenClimateRecord != null) {
      getEntityManager().remove(givenClimateRecord);
      return givenClimateRecord;
    }

    return null;
  }

  /**
   * Modificar los valores de un registro climatico
   *
   * @param int id [ID del registro climatico a modificar]
   * @param ClimateRecord modifiedClimateRecord [registro climatico con los valores con los que realizar la modificacion]
   * @return referencia a un objeto de tipo ClimateRecord en caso de realizarse la modificacion, retorna null en caso no encontrar
   * el registro climatico con el ID dado
   */
  public ClimateRecord modify(int id, ClimateRecord modifiedClimateRecord) {
    ClimateRecord chosenClimateRecord = find(id);

    if (chosenClimateRecord != null) {
      chosenClimateRecord.setDate(modifiedClimateRecord.getDate());
      chosenClimateRecord.setTemperatureMin(modifiedClimateRecord.getTemperatureMin());
      chosenClimateRecord.setTemperatureMax(modifiedClimateRecord.getTemperatureMax());
      chosenClimateRecord.setParcel(modifiedClimateRecord.getParcel());

      return chosenClimateRecord;
    }

    return null;
  }

  public ClimateRecord find(int id) {
    return getEntityManager().find(ClimateRecord.class, id);
  }

  /**
   * Retorna un registro climatico perteneciente a una de las
   * parcelas de un usuario
   * 
   * @param userId
   * @param climateRecordId
   * @return referencia a un objeto de tipo ClimateRecord perteneciente
   * a una parcela del usuario con el ID dado
   */
  public ClimateRecord find(int userId, int climateRecordId) {
    Query query = getEntityManager().createQuery("SELECT c FROM ClimateRecord c JOIN c.parcel p WHERE (c.id = :climateRecordId AND p.user.id = :userId)");
    query.setParameter("climateRecordId", climateRecordId);
    query.setParameter("userId", userId);

    return (ClimateRecord) query.getSingleResult();
  }

  /**
   * Retorna los registros climaticos de las parcelas de un
   * usuario
   * 
   * @param userId
   * @return referencia a un objeto de tipo Collection que
   * contiene los registros climaticos de las parcelas
   * pertenecientes al usuario con el ID dado
   */
  public Collection<ClimateRecord> findAll(int userId) {
    Query query = getEntityManager().createQuery("SELECT c FROM ClimateRecord c JOIN c.parcel p WHERE (p.user.id = :userId) ORDER BY c.id");
    query.setParameter("userId", userId);

    return (Collection) query.getResultList();
  }

  /**
   * Comprueba si un registro climatico pertenece a un usuario
   * dado, mediante la relacion muchos a uno que hay entre los
   * modelos de datos ClimateRecord y Parcel.
   * 
   * Retorna true si y solo si el registro climatico pertenece
   * al usuario dado.
   * 
   * @param userId
   * @param climateRecordId
   * @return true si se encuentra el registro climatico con el
   * ID y el ID de usuario provistos, false en caso contrario
   */
  public boolean checkUserOwnership(int userId, int climateRecordId) {
    boolean result = false;

    try {
      find(userId, climateRecordId);
      result = true;
    } catch (NoResultException e) {
      e.printStackTrace();
    }

    return result;
  }

}

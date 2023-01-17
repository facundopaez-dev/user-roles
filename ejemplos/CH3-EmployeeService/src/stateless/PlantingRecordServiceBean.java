package stateless;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Parcel;
import model.PlantingRecord;

@Stateless
public class PlantingRecordServiceBean {

  @PersistenceContext(unitName="EmployeeService")
  protected EntityManager entityManager;

  public void setEntityManager(EntityManager emLocal) {
    entityManager = emLocal;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  /**
   * Persiste un registro de plantacion en la base datos
   * @param PlantingRecord newPlantingRecord
   * @return PlantingRecord se retorna la PlantingRecord persistida en la base de datos
   */
  public PlantingRecord create(PlantingRecord newPlantingRecord) {
    entityManager.persist(newPlantingRecord);
    return newPlantingRecord;
  }

  /**
   * Elimina un registro de plantacion perteneciente a una parcela
   * de un usuario
   * 
   * @param userId
   * @param plantingRecordId
   * @return referencia a un objeto de tipo PlantingRecord si el
   * registro de plantacion a eliminar pertenece a una parcela del
   * usuario con el ID dado, null en caso contrario
   */
  public PlantingRecord remove(int userId, int plantingRecordId) {
    PlantingRecord givenPlantingRecord = find(userId, plantingRecordId);

    if (givenPlantingRecord != null) {
      getEntityManager().remove(givenPlantingRecord);
      return givenPlantingRecord;
    }

    return null;
  }

  /**
   * Modifica un registro de plantacion perteneciente a una parcela
   * de un usuario
   * 
   * @param userId
   * @param plantingRecordId
   * @param modifiedPlantingRecord
   * @return referencia a un objeto de tipo PlantingRecord si el registro de plantacion
   * a modificar pertenece a una parcela del usuario con el ID dado, null en caso
   * contrario
   */
  public PlantingRecord modify(int userId, int plantingRecordId, PlantingRecord modifiedPlantingRecord) {
    PlantingRecord choosenPlantingRecord = find(userId, plantingRecordId);

    if (choosenPlantingRecord != null) {
      choosenPlantingRecord.setSeedDate(modifiedPlantingRecord.getSeedDate());
      choosenPlantingRecord.setHarvestDate(modifiedPlantingRecord.getHarvestDate());
      choosenPlantingRecord.setParcel(modifiedPlantingRecord.getParcel());
      return choosenPlantingRecord;
    }

    return null;
  }

  /**
   * Retorna los registros de plantacion de las parcelas
   * de un usuario
   * 
   * @param userId
   * @return referencia a un objeto de tipo Collection que
   * contiene los registros de plantacion de las parcelas
   * del usuario con el ID dado
   */
  public Collection<PlantingRecord> findAll(int userId) {
    Query query = getEntityManager().createQuery("SELECT r FROM PlantingRecord r JOIN r.parcel p WHERE (p.user.id = :userId) ORDER BY r.id");
    query.setParameter("userId", userId);

    return (Collection) query.getResultList();
  }

  /**
   * Retorna un registro de plantacion perteneciente a una de las
   * parcelas de un usuario
   * 
   * @param userId
   * @param plantingRecordId
   * @return referencia a un objeto de tipo PlantingRecord perteneciente
   * a una parcela del usuario con el ID dado
   */
  public PlantingRecord find(int userId, int plantingRecordId) {
    Query query = getEntityManager().createQuery("SELECT r FROM PlantingRecord r JOIN r.parcel p WHERE (r.id = :plantingRecordId AND p.user.id = :userId)");
    query.setParameter("plantingRecordId", plantingRecordId);
    query.setParameter("userId", userId);

    return (PlantingRecord) query.getSingleResult();
  }

  /**
   * Comprueba si un registro de plantacion pertenece a un usuario
   * dado, mediante la relacion muchos a uno que hay entre los
   * modelos de datos PlantingRecord y Parcel.
   * 
   * Retorna true si y solo si el registro de plantacion pertenece
   * al usuario dado.
   * 
   * @param userId
   * @param plantingRecordId
   * @return true si se encuentra el registro de plantacion con el
   * ID y el ID de usuario provistos, false en caso contrario
   */
  public boolean checkUserOwnership(int userId, int plantingRecordId) {
    boolean result = false;

    try {
      find(userId, plantingRecordId);
      result = true;
    } catch (NoResultException e) {
      e.printStackTrace();
    }

    return result;
  }

}

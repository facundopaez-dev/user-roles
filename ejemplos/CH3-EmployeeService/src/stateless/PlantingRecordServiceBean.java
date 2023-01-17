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
   * Elimina el registro de plantacion que tiene el ID dado
   * @param int id que identifica a un registro de plantacion
   * @return referencia a un objeto de tipo PlantingRecord si se elimina un registro de
   * plantacion con el ID dado, en caso retorna null
   */
  public PlantingRecord remove(int id) {
    PlantingRecord givenPlantingRecord = find(id);

    if (givenPlantingRecord != null) {
      entityManager.remove(givenPlantingRecord);
      return givenPlantingRecord;
    }

    return null;
  }

  /**
   * Modifica los valores de un registro de plantacion identificado con el ID dado
   * @param  id
   * @param  modifiedPlantingRecord
   * @return referencia a un objeto de tipo PlantingRecord con los datos modificados si se
   * encuentra el registro de plantacion con el identificador dado, en caso contrario retorna null
   */
  public PlantingRecord modify(int id, PlantingRecord modifiedPlantingRecord) {
    PlantingRecord choosenInstance = find(id);

    if (choosenInstance != null) {
      if (choosenInstance.getId() != modifiedPlantingRecord.getId()) {
        return null;
      }

      choosenInstance.setSeedDate(modifiedPlantingRecord.getSeedDate());
      choosenInstance.setHarvestDate(modifiedPlantingRecord.getHarvestDate());
      choosenInstance.setParcel(modifiedPlantingRecord.getParcel());
      return choosenInstance;
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
   * @param  id [ID de un registro de plantacion]
   * @return referencia a un objeto de tipo PlantingRecord en caso de encontrar
   * en la base de datos subyacente el registro de plantacion con el ID dado, en caso
   * contrario retorna null
   */
  public PlantingRecord find(int id) {
    return entityManager.find(PlantingRecord.class, id);
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

package stateless;

import java.util.Collection;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.PlantingRecord;
import model.Parcel;

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

  public Collection<PlantingRecord> findAll() {
    Query query = getEntityManager().createQuery("SELECT p FROM PlantingRecord p");
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

}

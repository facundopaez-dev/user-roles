package stateless;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

}

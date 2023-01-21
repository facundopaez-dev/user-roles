package stateless;

import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Crop;

@Stateless
public class CropServiceBean {

  @PersistenceContext(unitName = "EmployeeService")
  protected EntityManager entityManager;

  public void setEntityManager(EntityManager emLocal) {
    entityManager = emLocal;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public Crop create(Crop newCrop) {
    entityManager.persist(newCrop);
    return newCrop;
  }

  public Crop remove(int id) {
    Crop crop = find(id);

    if (crop != null) {
      getEntityManager().remove(crop);
      return crop;
    }

    return null;
  }

  public Crop modify(int id, Crop modifiedCrop) {
    Crop choosenCrop = find(id);

    if (choosenCrop != null) {
      choosenCrop.setName(modifiedCrop.getName());
      choosenCrop.setCropCoefficient(modifiedCrop.getCropCoefficient());
      choosenCrop.setLifeCycle(modifiedCrop.getLifeCycle());
      return choosenCrop;
    }

    return null;
  }

  public Crop find(int id) {
    return getEntityManager().find(Crop.class, id);
  }

  public Collection<Crop> findAll() {
    Query query = entityManager.createQuery("SELECT c FROM Crop c ORDER BY c.id");
    return (Collection) query.getResultList();
  }

  /**
   * Comprueba la existencia de un cultivo en la base de datos
   * subyacente. Retorna true si y solo si existe el cultivo
   * con el ID dado.
   * 
   * @param id
   * @return true si el cultivo con el ID dado existe en la
   * base de datos subyacente, false en caso contrario
   */
  public boolean checkExistence(int id) {
    return (getEntityManager().find(Crop.class, id) != null);
  }

}

package stateless;

import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Permission;

@Stateless
public class PermissionServiceBean {

  @PersistenceContext(unitName="EmployeeService")
  protected EntityManager em;

  public void setEntityManager(EntityManager emLocal) {
    em = emLocal;
  }

  public EntityManager getEntityManager() {
    return em;
  }

  public Permission find(int id) {
    return getEntityManager().find(Permission.class, id);
  }

  public Collection<Permission> findAll() {
    Query query = getEntityManager().createQuery("SELECT p FROM Permission p");
    return (Collection) query.getResultList();
  }

}

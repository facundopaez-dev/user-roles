package stateless;

import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.User;

@Stateless
public class UserServiceBean {

  @PersistenceContext(unitName="EmployeeService")
  protected EntityManager em;

  public void setEntityManager(EntityManager emLocal) {
    em = emLocal;
  }

  public EntityManager getEntityManager() {
    return em;
  }

  public User create(User newUser) {
    getEntityManager().persist(newUser);
    return newUser;
  }

  public User find(int id) {
    return getEntityManager().find(User.class, id);
  }

  public Collection<User> findAll() {
    Query query = getEntityManager().createQuery("SELECT u FROM User u");
    return (Collection) query.getResultList();
  }

  public User validate(String username, String password) {
    Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE UPPER(u.userName) = UPPER(:username) AND UPPER(u.password) = UPPER(:password)");
    query.setParameter("username", username);
    query.setParameter("password", password);

    return (User) query.getSingleResult();
  }

}

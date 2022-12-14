package stateless;

import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.User;

@Stateless
public class UserServiceBean {

  @PersistenceContext(unitName = "EmployeeService")
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

  public User remove(int id) {
    User givenUser = find(id);

    if (givenUser != null) {
      getEntityManager().remove(givenUser);
      return givenUser;
    }

    return null;
  }

  public User find(int id) {
    return getEntityManager().find(User.class, id);
  }

  public Collection<User> findAll() {
    Query query = getEntityManager().createQuery("SELECT u FROM User u");
    return (Collection) query.getResultList();
  }

  /**
   * Busca un usuario en la base de datos subyacente mediante un nombre de usuario
   *
   * @param username el nombre de usuario que se usa para buscar en la base de datos subyacente, el usuario que tiene el nombre de usuario provisto
   * @return referencia a un objeto de tipo User en caso de encontrarse en la base de datos subyacente,
   * el usuario con el nombre de usuario provisto, null en caso contrario
   */
  public User findByUsername(String username) {
    Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE UPPER(u.username) = UPPER(:username)");
    query.setParameter("username", username);

    User user = null;

    try {
      user = (User) query.getSingleResult();
    } catch (NoResultException e) {
      e.printStackTrace();
    }

    return user;
  }

  /**
   * Comprueba si el usuario con el nombre de usuario provisto, tiene el permiso de administrador (super usuario)
   *
   * @param username el nombre de usuario que se usa para comprobar si el usuario con el nombre de usuario
   * provisto, tiene el permiso de administrador
   * @return true si el usuario con el nombre de usuario provisto, tiene el permiso de administrador, false en caso contrario
   */
  public boolean checkSuperuserPermission(String username) {
    User givenUser = findByUsername(username);
    return givenUser.getSuperuser();
  }

  /**
   * Realiza la autenticacion de un usuario mediante el nombre de usuario y la contrase??a que provee
   *
   * @param username el nombre de usuario que se usa para autentificar (demostrar que el usuario es quien dice ser) la cuenta del usuario que inicia sesion
   * @param password la contrase??a que se usa para autentificar (demostrar que el usuario es quien dice ser) la cuenta del usuario que inicia sesion
   * @return true si el nombre de usuario y la contrase??a provistos son iguales al nombre de usuario y la contrase??a que el usuario registro en la base de datos,
   * false en caso contrario
   */
  public boolean authenticate(String username, String password) {
    Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE UPPER(u.username) = UPPER(:username) AND UPPER(u.password) = UPPER(:password)");
    query.setParameter("username", username);
    query.setParameter("password", password);

    boolean result = false;

    try {
      query.getSingleResult();
      result = true;
    } catch (NoResultException e) {
      e.printStackTrace();
    }

    return result;
  }
  
}

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
   * Retorna el usuario que tiene el correo electronico dado si y solo
   * existe un usuario con ese correo electronico en la base de datos
   * subyacente.
   * 
   * @param email
   * @return referencia a un objeto de tipo User que contiene el usuario
   * correspondiente al correo electronico dado, si existe en la base de
   * datos subyacente un usuario con dicho correo electronico. En caso
   * contrario, null. Tambien retorna null en el caso en el que el
   * parametro contenga el valor null.
   */
  private User findByEmail(String email) {
    /*
     * Si la variable de tipo por referencia email de tipo String
     * contiene el valor null, se retorna el valor null.
     * 
     * Con este control se evita realizar una consulta a la base
     * de datos comparando el correo electronico con el valor
     * null. Si no se realiza este control y se realiza esta
     * consulta a la base de datos comparando el correo
     * electronico con el valor null, ocurre la excepcion
     * SQLSyntaxErrorException, debido a que la comparacion de
     * un atributo con el valor null incumple la sintaxis del
     * proveedor del motor de base de datos.
     */
    if (email == null) {
      return null;
    }

    Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE UPPER(u.email) = UPPER(:email)");
    query.setParameter("email", email);

    User user = null;

    try {
      user = (User) query.getSingleResult();
    } catch (NoResultException e) {
      e.printStackTrace();
    }

    return user;
  }

  /**
   * Retorna true si y solo si existe un usuario con el correo electronico
   * dado, registrado en la base de datos subyacente
   * 
   * @param email
   * @return true si hay un usuario con el correo electronico dado,
   * registrado en la base de datos subyacente. En caso contrario, false.
   */
  public boolean emailIsRegistered(String email) {
    User givenUser = findByEmail(email);

    if (givenUser == null) {
      return false;
    }

    return true;
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
   * Realiza la autenticacion de un usuario mediante el nombre de usuario y la contraseña que provee
   *
   * @param username el nombre de usuario que se usa para autentificar (demostrar que el usuario es quien dice ser) la cuenta del usuario que inicia sesion
   * @param password la contraseña que se usa para autentificar (demostrar que el usuario es quien dice ser) la cuenta del usuario que inicia sesion
   * @return true si el nombre de usuario y la contraseña provistos son iguales al nombre de usuario y la contraseña que el usuario registro en la base de datos,
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

  /**
   * Comprueba la existencia de un usuario en la base de datos
   * subyacente. Retorna true si y solo si existe el usuario
   * con el ID dado.
   * 
   * @param id
   * @return true si el usuario con el ID dado existe en la
   * base de datos subyacente, false en caso contrario
   */
  public boolean checkExistence(int id) {
    return (getEntityManager().find(User.class, id) != null);
  }

  /**
   * Retorna true si y solo si el usuario asociado a la direccion de
   * correo electronico dada, esta registrado en la base de datos
   * subyacente y esta acitvo
   * 
   * @param email
   * @return true si el usuario correspondiente a la direccion de
   * correo electronico dada, esta activo, y false en el caso en el que
   * NO esta activo o NO esta registrado en la base de datos subyacente.
   */
  public boolean isActive(String email) {
    User givenUser = findByEmail(email);

    /*
     * Si el usuario correspondiente a la direccion de correo
     * electronico dada, existe y esta activo, se retorna el
     * valor booleano true
     */
    if (givenUser != null && givenUser.getActive()) {
      return true;
    }

    return false;
  }

}

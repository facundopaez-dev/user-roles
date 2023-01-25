package stateless;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Session;

@Stateless
public class SessionServiceBean {

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

  public Session create(Session newSession) {
    getEntityManager().persist(newSession);
    return newSession;
  }

  /**
   * Elimina logicamente la sesion actual que abrio el usuario
   * con el ID dado. Esta eliminacion logica se realiza cuando
   * el usuario cierra su sesion actual.
   * 
   * Se denomina "sesion actual" a la ultima sesion vigente
   * que tiene un usuario.
   * 
   * Cuando el usuario cierra la sesion que tiene abierta sin que
   * esta haya llegado a su tiempo de expiracion, se registra
   * este cierre en la base de datos. Si no se hace esto, el
   * usuario no puede iniciar sesion nuevamente, debido a que
   * la sesion que cerro todavia no ha llegado a su tiempo de
   * expiracion.
   * 
   * Una sesion que NO ha llegado a su tiempo de expiracion,
   * pero que fue cerrada, es una sesion NO vigente.
   * 
   * Una sesion que ha llegado a su tiempo de expiracion,
   * pero que NO fue cerrada, es una sesion NO vigente.
   * 
   * @param userId
   * @return referencia a un objeto de tipo Session con su
   * variable closed en false, si se encuentra la sesion
   * del usuario con el ID dado, null en caso contrario
   */
  public Session remove(int userId) {
    Session givenSession = findLastSession(userId);

    if (givenSession != null) {
      givenSession.setClosed(true);
      return givenSession;
    }

    return null;
  }

  /**
   * Retorna la ultima sesion que abrio el usuario con el
   * ID dado.
   * 
   * @param userId
   * @return referencia a un objeto de tipo Session si
   * se encuentra la ultima sesion que abrio el usuario
   * con el ID dado, null en caso contrario
   */
  public Session findLastSession(int userId) {
    Query query = getEntityManager().createQuery("SELECT s FROM Session s WHERE s.id = (SELECT MAX(s.id) FROM Session s JOIN s.user p WHERE p.id = :userId)");
    query.setParameter("userId", userId);

    Session givenSession = null;

    try {
      givenSession = (Session) query.getSingleResult();
    } catch (NoResultException e) {
      e.printStackTrace();
    }

    return givenSession;
  }

  /**
   * Elimina fisicamente la sesion que tiene el ID dado.
   * 
   * @param sessionId
   * @return referencia a un objeto de tipo Session si existe
   * una sesion con el ID dado, null en caso contrario
   */
  public Session delete(int sessionId) {
    Session givenSession = getEntityManager().find(Session.class, sessionId);

    if (givenSession != null) {
      getEntityManager().remove(givenSession);
      return givenSession;
    }

    return null;
  }

}
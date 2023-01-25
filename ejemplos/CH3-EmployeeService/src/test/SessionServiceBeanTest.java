import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Session;
import model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import stateless.SessionServiceBean;
import stateless.UserServiceBean;

public class SessionServiceBeanTest {
  private static SessionServiceBean sessionService;
  private static UserServiceBean userService;
  private static EntityManager entityManager;
  private static EntityManagerFactory entityManagerFactory;
  private static Collection<Session> sessions;
  private static Collection<User> users;

  @BeforeClass
  public static void preTest() {
    entityManagerFactory = Persistence.createEntityManagerFactory("EmployeeService");
    entityManager = entityManagerFactory.createEntityManager();
    sessionService = new SessionServiceBean();
    userService = new UserServiceBean();

    sessionService.setEntityManager(entityManager);
    sessions = new ArrayList<>();

    userService.setEntityManager(entityManager);
    users = new ArrayList<>();
  }

  @Test
  public void testOneFindLastSession() {
    System.out.println("******************* Prueba uno del metodo findLastSession *******************");
    System.out.println("- En esta prueba se utiliza un usuario que no tiene ningun inicio de sesion,");
    System.out.println("con lo cual, no tiene ninguna sesion abierta registrada en la base de datos");
    System.out.println("subyacente. En consecuencia, este usuario no tiene registrada una ultima sesion");
    System.out.println("abierta.");
    System.out.println();
    System.out.println("Por lo tanto, el metodo findLastSession de la clase SessionServiceBean retorna");
    System.out.println("el valor null.");
    System.out.println();

    User user = new User();
    user.setUsername("Dylan");
    user.setPassword("Dylan");

    /*
     * Se persiste el dato creado en la base de
     * datos subyacente
     */
    entityManager.getTransaction().begin();
    user = userService.create(user);
    entityManager.getTransaction().commit();

    /*
     * El dato persistido es agregado a una coleccion
     * para su posterior eliminacion de la base de datos
     * subyacente
     */
    users.add(user);

    /*
     * Seccion de prueba.
     * 
     * El usuario persistido no tiene ninguna sesion registrada
     * en la base de datos subyacente. Por lo tanto, el metodo
     * findLastSession de la clase SessionServiceBean debe retornar
     * null.
     */
    Session lastSession = sessionService.findLastSession(user.getId());

    System.out.println("Valor esperado: " + null);
    System.out.println("* Valor devuelto por el metodo findLastSession: " + lastSession);
    System.out.println();

    assertNull(lastSession);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testTwoFindLastSession() {
    System.out.println("******************* Prueba uno del metodo findLastSession *******************");
    System.out.println("- En este prueba se utiliza un usuario que tiene tres inicios de sesion, con lo");
    System.out.println("cual, tiene tres inicios de sesion registrados en la base de datos subyacente.");
    System.out.println("En consecuencia, este usuario tiene registrada una ultima sesion abierta.");
    System.out.println();
    System.out.println("Por lo tanto, el metodo findLastSession de la clase SessionServiceBean retorna");
    System.out.println("la ultima sesion abierta.");
    System.out.println();

    User user = new User();
    user.setUsername("Lazaro");
    user.setPassword("Lazaro");

    Session firstSession = new Session();
    firstSession.setDateIssue(Calendar.getInstance());
    firstSession.setExpirationDate(Calendar.getInstance());
    firstSession.setUser(user);

    Session secondSession = new Session();
    secondSession.setDateIssue(Calendar.getInstance());
    secondSession.setExpirationDate(Calendar.getInstance());
    secondSession.setUser(user);

    Session thirdSession = new Session();
    thirdSession.setDateIssue(Calendar.getInstance());
    thirdSession.setExpirationDate(Calendar.getInstance());
    thirdSession.setUser(user);

    /*
     * Se persisten los datos en la base de datos
     * subyacente
     */
    entityManager.getTransaction().begin();
    user = userService.create(user);
    entityManager.getTransaction().commit();

    entityManager.getTransaction().begin();
    firstSession = sessionService.create(firstSession);
    entityManager.getTransaction().commit();

    entityManager.getTransaction().begin();
    secondSession = sessionService.create(secondSession);
    entityManager.getTransaction().commit();

    entityManager.getTransaction().begin();
    thirdSession = sessionService.create(thirdSession);
    entityManager.getTransaction().commit();

    /*
     * Los datos persistidos son agreados a una coleccion
     * para su posterior eliminacion de la base de datos
     * subyacente
     */
    users.add(user);
    sessions.add(firstSession);
    sessions.add(secondSession);
    sessions.add(thirdSession);

    /*
     * Seccion de prueba.
     * 
     * El usuario persistido tiene varias sesiones abiertas registradas
     * en la base de datos subyacente. Por lo tanto, el metodo
     * findLastSession de la clase SessionServiceBean debe retornar
     * la ultima sesion que abrio dicho usuario.
     */
    Session lastSession = sessionService.findLastSession(user.getId());

    /*
     * El valor esperado es el ID de la tercera sesion porque
     * esta es la ultima sesion abierta que se registra para
     * el usuario dado
     */
    System.out.println("Valor esperado: " + thirdSession.getId());
    System.out.println("* Valor devuelto por el metodo findLastSession, ID de la ultima sesion: " + lastSession.getId());
    System.out.println();

    assertEquals(lastSession.getId(), thirdSession.getId());

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @AfterClass
  public static void postTest() {
    entityManager.getTransaction().begin();

    /*
     * Elimina de la base de datos subyacente los datos persistidos
     * durante la ejecucion de las pruebas unitarias para dejarla en
     * su estado original, es decir, para dejarla en el estado en el
     * que estaba antes de que se persistieran dichos datos
     */
    for (Session currentSession : sessions) {
      sessionService.delete(currentSession.getId());
    }

    for (User currentUser : users) {
      userService.remove(currentUser.getId());
    }

    entityManager.getTransaction().commit();

    // Cierra las conexiones
    entityManager.close();
    entityManagerFactory.close();
  }

}

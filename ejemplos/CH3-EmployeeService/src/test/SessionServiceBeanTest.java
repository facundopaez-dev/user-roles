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
import stateless.SecretKeyServiceBean;
import stateless.SessionServiceBean;
import stateless.UserServiceBean;
import utilJwt.JwtManager;

public class SessionServiceBeanTest {
  private static SessionServiceBean sessionService;
  private static UserServiceBean userService;
  private static SecretKeyServiceBean secretKeyService;
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
    secretKeyService = new SecretKeyServiceBean();

    sessionService.setEntityManager(entityManager);
    sessions = new ArrayList<>();

    userService.setEntityManager(entityManager);
    users = new ArrayList<>();

    secretKeyService.setEntityManager(entityManager);
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

    assertNull(lastSession);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testTwoFindLastSession() {
    System.out.println("******************* Prueba dos del metodo findLastSession *******************");
    System.out.println("- En esta prueba se utiliza un usuario que tiene tres inicios de sesion, con lo");
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

    assertEquals(lastSession.getId(), thirdSession.getId());

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testOneCheckActiveSession() {
    System.out.println("******************* Prueba uno del metodo checkActiveSession *******************");
    System.out.println("- En esta prueba se utiliza un usuario que no tiene ninguna sesion registrada");
    System.out.println("en la base de datos subyacente. El metodo checkActiveSession de la clase");
    System.out.println("SessionServiceBean retorna true cuando un usuario tiene una sesion activa, y");
    System.out.println("false cuando NO tiene una sesion activa.");
    System.out.println();
    System.out.println("En este caso, como el usuario no tiene ninguna sesion registrada en la base de");
    System.out.println("datos, ni activa ni inactiva, el metodo checkActiveSession retorna el valor");
    System.out.println("booleano false.");
    System.out.println();

    User newUser = new User();
    newUser.setUsername("Teresa");
    newUser.setPassword("Teresa");

    /*
     * El dato creado es persistido en la base de datos
     * subyacente
     */
    entityManager.getTransaction().begin();
    newUser = userService.create(newUser);
    entityManager.getTransaction().commit();

    printUserData(newUser);

    /*
     * Se agrega el dato creado a una coleccion para su posterior
     * eliminacion de la base de datos subyacente, lo cual se hace
     * para que la misma tenga el estado que tenia antes de la ejecucion
     * de esta prueba unitaria
     */
    users.add(newUser);

    /*
     * Seccion de prueba
     */
    boolean activeSession = sessionService.checkActiveSession(newUser.getId());

    System.out.println("Valor esperado: " + false);
    System.out.println("* Valor devuelto por el metodo checkActiveSession: " + activeSession);

    assertFalse(activeSession);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testTwoCheckActiveSession() {
    System.out.println("******************* Prueba dos del metodo checkActiveSession *******************");
    System.out.println("- En esta prueba se utiliza un usuario que tiene una unica sesion registrada en la");
    System.out.println("base de datos subyacente, la cual esta inactiva debido a que expiro. El metodo");
    System.out.println("checkActiveSession de la clase SessionServiceBean retorna true cuando un usuario");
    System.out.println("tiene una sesion activa, y false cuando NO tiene una sesion activa.");
    System.out.println();
    System.out.println("Por lo tanto, el metodo checkActiveSession retorna el valor booleano false.");
    System.out.println();
    System.out.println("Nota: Una sesion tiene un limite de tiempo en el cual esta activa. Una vez que");
    System.out.println("se cumple ese limite de tiempo, la sesion expira, motivo por el cual se la");
    System.out.println("considera como inactiva.");
    System.out.println();

    /*
     * Creacion de un usuario
     */
    User newUser = new User();
    newUser.setUsername("Beca");
    newUser.setPassword("Beca");

    /*
     * Creacion de una sesion.
     * 
     * Una sesion que tiene su fecha de expiracion antes de la fecha
     * actual del sistema, es una sesion expirada, motivo por el
     * cual se considera como una sesion inactiva.
     * 
     * En este caso, se crea una sesion y a su fecha de expiracion
     * se le resta un segundo para hacer que este antes de la
     * fecha actual del sistema. Esto se hace para que la sesion
     * que tiene esta fecha de expiracion, sea una sesion expirada.
     */
    Calendar dateIssue = Calendar.getInstance();

    Calendar expirationDate = Calendar.getInstance();
    expirationDate.setTimeInMillis(expirationDate.getTimeInMillis() - 1000);

    Session newSession = new Session();
    newSession.setUser(newUser);
    newSession.setDateIssue(dateIssue);
    newSession.setExpirationDate(expirationDate);

    /*
     * Los datos creados son persistidos en la base de datos
     * subyacente
     */
    entityManager.getTransaction().begin();
    newUser = userService.create(newUser);
    newSession = sessionService.create(newSession);
    entityManager.getTransaction().commit();

    printUserData(newUser);

    /*
     * Los datos persistidos son agregados a su correspondiente coleccion
     * para su posterior eliminacion de la base de datos subyacente, lo
     * cual se hace para que la misma tenga el estado que tenia antes de
     * la ejecucion de esta prueba unitaria
     */
    users.add(newUser);
    sessions.add(newSession);

    /*
     * Seccion de prueba
     */
    boolean activeSession = sessionService.checkActiveSession(newUser.getId());

    System.out.println("Valor esperado: " + false);
    System.out.println("* Valor devuelto por el metodo checkActiveSession: " + activeSession);

    assertFalse(activeSession);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testThreeCheckActiveSession() {
    System.out.println("******************* Prueba tres del metodo checkActiveSession *******************");
    System.out.println("- En esta prueba se utiliza un usuario que tiene una unica sesion registrada en la");
    System.out.println("base de datos subyacente, la cual esta inactiva, no por expiracion, sino porque");
    System.out.println("fue cerrada por el usuario. El metodo checkActiveSession de la clase");
    System.out.println("SessionServiceBean retorna true cuando un usuario tiene una sesion activa, y false");
    System.out.println("cuando NO tiene una sesion activa.");
    System.out.println();
    System.out.println("Por lo tanto, el metodo checkActiveSession retorna el valor booleano false.");
    System.out.println();
    System.out.println("Nota: Una sesion que no ha expirado, pero que fue cerrada por el usuario que la");
    System.out.println("abrio, es una sesion que se considera como inactiva.");
    System.out.println();

    /*
     * Creacion de un usuario
     */
    User newUser = new User();
    newUser.setUsername("Dom");
    newUser.setPassword("Dom");

    /*
     * Creacion de una sesion.
     * 
     * Cuando un usuario cierra su sesion, se establece en false
     * la variable de instancia closed de un objeto de tipo Session.
     * De esta manera, una sesion activa pasa a ser inactiva.
     * 
     * En este caso, la sesion creada no expiro debido a que el
     * tiempo de expiracion esta despues de la fecha actual del
     * sistema, pero esta cerrada, con lo cual, es una sesion
     * inactiva.
     */
    Calendar dateIssue = Calendar.getInstance();
    Calendar expirationDate = Calendar.getInstance();

    Session newSession = new Session();
    newSession.setUser(newUser);
    newSession.setDateIssue(dateIssue);
    newSession.setExpirationDate(expirationDate);
    newSession.setClosed(true);

    /*
     * Los datos creados son persistidos en la base de datos
     * subyacente
     */
    entityManager.getTransaction().begin();
    newUser = userService.create(newUser);
    newSession = sessionService.create(newSession);
    entityManager.getTransaction().commit();

    printUserData(newUser);

    /*
     * Los datos persistidos son agregados a su correspondiente coleccion
     * para su posterior eliminacion de la base de datos subyacente, lo
     * cual se hace para que la misma tenga el estado que tenia antes de
     * la ejecucion de esta prueba unitaria
     */
    users.add(newUser);
    sessions.add(newSession);

    /*
     * Seccion de prueba
     */
    boolean activeSession = sessionService.checkActiveSession(newUser.getId());

    System.out.println("Valor esperado: " + false);
    System.out.println("* Valor devuelto por el metodo activeSession: " + activeSession);

    assertFalse(activeSession);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testFourActiveSession() {
    System.out.println("******************* Prueba cuatro del metodo checkActiveSession *******************");
    System.out.println("- En esta prueba se utiliza un usuario que tiene una unica sesion registrada en la");
    System.out.println("base de datos subyacente, la cual esta activa, ya que no expiro ni fue cerrada por");
    System.out.println("el usuario. El metodo checkActiveSession de la clase SessionServiceBean retorna true");
    System.out.println("cuando un usuario tiene una sesion activa, y false cuando NO tiene una sesion activa.");
    System.out.println();
    System.out.println("Por lo tanto, el metodo checkActiveSession retorna el valor booleano true.");
    System.out.println();
    System.out.println("Nota: Una sesion que no ha expirado, ni que fue cerrada por el usuario que la abrio,");
    System.out.println("es una sesion que se considera como activa.");
    System.out.println();

    /*
     * Creacion de un usuario
     */
    User newUser = new User();
    newUser.setUsername("Violet");
    newUser.setPassword("Violet");

    /*
     * Creacion de un JWT con el usuario creado
     */
    String jwt = JwtManager.createJwt(newUser.getId(), newUser.getSuperuser(), secretKeyService.find().getValue());

    /*
     * Creacion de una sesion.
     * 
     * La fecha de emision es la fecha en la que se crea un
     * JWT. La fecha de expiracion es la fecha de emision
     * mas 15 minutos (ver clase JwtManager).
     * 
     * Una sesion que se crea con estas fechas es una sesion
     * no expirada, siempre y cuando su fecha de expiracion
     * NO este antes de la fecha actual del sistema.
     * 
     * En este caso, la fecha de expiracion esta despues de
     * la fecha actual del sistema, motivo por el se considera
     * como activa a la sesion que use esta fecha de expiracion.
     */
    Session newSession = new Session();
    newSession.setUser(newUser);
    newSession.setDateIssue(JwtManager.getDateIssue(jwt, secretKeyService.find().getValue()));
    newSession.setExpirationDate(JwtManager.getExpirationDate(jwt, secretKeyService.find().getValue()));

    /*
     * Los datos creados son persistidos en la base de datos
     * subyacente
     */
    entityManager.getTransaction().begin();
    newUser = userService.create(newUser);
    newSession = sessionService.create(newSession);
    entityManager.getTransaction().commit();

    printUserData(newUser);

    /*
     * Los datos persistidos son agregados a su correspondiente coleccion
     * para su posterior eliminacion de la base de datos subyacente, lo
     * cual se hace para que la misma tenga el estado que tenia antes de
     * la ejecucion de esta prueba unitaria
     */
    users.add(newUser);
    sessions.add(newSession);

    /*
     * Seccion de prueba
     */
    boolean activeSession = sessionService.checkActiveSession(newUser.getId());

    System.out.println("Valor esperado: " + true);
    System.out.println("* Valor devuelto por el metodo activeSession: " + activeSession);

    assertTrue(activeSession);

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

  /**
   * Imprime los datos de un usuario
   * 
   * @param givenUser
   */
  private void printUserData(User givenUser) {
    System.out.println("Datos del usuario persistido");
    System.out.println("ID: " + givenUser.getId());
    System.out.println("Nombre de usuario: " + givenUser.getUsername());
    System.out.println("Contraseña: " + givenUser.getPassword());
    System.out.println("Permiso de super usuario (administrador): " + givenUser.getSuperuser());
    System.out.println();
  }

}

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass;
import org.junit.Ignore;

import model.User;
import stateless.UserServiceBean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.ArrayList;

public class UserServiceBeanTest {
  private static UserServiceBean userService;
  private static EntityManager entityManager;
  private static EntityManagerFactory entityManagerFactory;
  private static Collection<User> users;

  @BeforeClass
  public static void preTest() {
    entityManagerFactory = Persistence.createEntityManagerFactory("EmployeeService");
    entityManager = entityManagerFactory.createEntityManager();
    userService = new UserServiceBean();
    userService.setEntityManager(entityManager);
    users = new ArrayList<>();
  }

  @Test
  public void testAuthenticateOne() {
    System.out.println("******************** Prueba uno del metodo authenticate() ********************");
    System.out.println("- En este prueba se realiza la autenticacion de un usuario existente en la base");
    System.out.println("de datos subyacente, y la contraseña utilizada es correcta. En este caso, el");
    System.out.println("el metodo authenticate() de la clase UserServiceBean debe retornar el valor booleano");
    System.out.println("true.");
    System.out.println();
    
    String username = "Stinger";
    String password = "Ultra secret password";
    
    System.out.println("Datos del usuario");
    System.out.println("Nombre de usuario: " + username);
    System.out.println("Contraseña: " + password);
    System.out.println();
    
    User newUser = new User();
    newUser.setUsername(username);
    newUser.setPassword(password);
    
    // Se persiste el usuario creado
    entityManager.getTransaction().begin();
    newUser = userService.create(newUser);
    entityManager.getTransaction().commit();
    
    users.add(newUser);
    
    System.out.println("Datos utilizados para la autenticacion del usuario");
    System.out.println("Nombre de usuario: " + username);
    System.out.println("Contraseña (correcta): " + password);
    System.out.println();
    
    assertTrue(userService.authenticate(username, password));
    
    System.out.println("* Prueba pasada satisfactoriamente.");
    System.out.println();
  }

  @Test
  public void testAuthenticateTwo() {
    System.out.println("******************** Prueba dos del metodo authenticate() ********************");
    System.out.println("- En este prueba se realiza la autenticacion de un usuario existente en la base");
    System.out.println("de datos subyacente, pero la contraseña utilizada es incorrecta. En este caso, el");
    System.out.println("metodo authenticate() de la clase UserServiceBean debe retornar el valor booleano");
    System.out.println("false.");
    System.out.println();

    String username = "secretadmin";
    String password = "secretpassword";
    String incorrectPassword = "secretpassword123";

    System.out.println("Datos del usuario");
    System.out.println("Nombre de usuario: " + username);
    System.out.println("Contraseña: " + password);
    System.out.println();
    
    User newUser = new User();
    newUser.setUsername(username);
    newUser.setPassword(password);
    
    // Se persiste el usuario creado
    entityManager.getTransaction().begin();
    newUser = userService.create(newUser);
    entityManager.getTransaction().commit();
    
    users.add(newUser);
    
    System.out.println("Datos utilizados para la autenticacion del usuario");
    System.out.println("Nombre de usuario: " + username);
    System.out.println("Contraseña (incorrecta): " + incorrectPassword);
    System.out.println();

    assertFalse(userService.authenticate(username, incorrectPassword));

    System.out.println("* Prueba pasada satisfactoriamente.");
    System.out.println();
  }

  @Test
  public void testAuthenticateThree() {
    System.out.println("******************** Prueba tres del metodo authenticate() ********************");
    System.out.println("- En este prueba se realiza la autenticacion de un usuario inexistente en la base");
    System.out.println("de datos subyacente. En este caso, el metodo authenticate() de la clase UserServiceBean");
    System.out.println("debe retornar el valor booleano false.");
    System.out.println();

    String username = "Javelin";
    String password = "Super secret password";

    System.out.println("Datos utilizados para la autenticacion del usuario (inexistente)");
    System.out.println("Nombre de usuario: " + username);
    System.out.println("Contraseña: " + password);
    System.out.println();
    
    assertFalse(userService.authenticate(username, password));

    System.out.println("* Prueba pasada satisfactoriamente.");
    System.out.println();
  }

  @Test
  public void testOneEmailIsRegistered() {
    System.out.println("*************************** Prueba uno del metodo emailIsRegistered ***************************");
    System.out.println("- En esta prueba se comprueba la existencia de un correo electronico en la base de datos");
    System.out.println("subyacente utilizando el correo electronico de un usuario registrado en la misma.");
    System.out.println("El metodo emailIsRegistered retorna true si un correo electronico esta registrado en la base de");
    System.out.println("datos subyacente, y false en caso contrario.");
    System.out.println();
    System.out.println("En este caso, el metodo emailIsRegistered retorna el valor booleano true.");
    System.out.println();

    /*
     * Se imprime por pantalla los datos de todos los
     * usuarios registrados en la base de datos subyacente
     */
    System.out.println("Usuarios registrados en la base de datos subyacente");
    printAllUsers();

    /*
     * Obtencion de un usuario registrado en la base
     * de datos subyacente
     */
    System.out.println("* Usuario de prueba");
    User givenUser = userService.find(2);
    printUserData(givenUser);

    /*
     * Seccion de prueba
     */
    boolean emailRegistered = userService.emailIsRegistered(givenUser.getEmail());

    System.out.println("Resultado esperado: " + true);
    System.out.println("* Valor obtenido: " + emailRegistered);

    assertTrue(emailRegistered);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testTwoEmailIsRegistered() {
    System.out.println("*************************** Prueba dos del metodo emailIsRegistered ***************************");
    System.out.println("- En esta prueba se comprueba la existencia de un correo electronico en la base de datos");
    System.out.println("subyacente utilizando un correo electronico inexistente en la misma.");
    System.out.println("El metodo emailIsRegistered retorna true si un correo electronico esta registrado en la base de");
    System.out.println("datos subyacente, y false en caso contrario.");
    System.out.println();
    System.out.println("En este caso, el metodo emailIsRegistered retorna el valor booleano false.");
    System.out.println();

    /*
     * Se imprime por pantalla los datos de todos los
     * usuarios registrados en la base de datos subyacente
     */
    System.out.println("Usuarios registrados en la base de datos subyacente");
    printAllUsers();

    /*
     * Correo electronico inexistente de prueba
     */
    String nonexistentEmail = "ghostemail@eservice.com";
    System.out.println("* Correo electronico inexistente en la base de datos subyacente: " + nonexistentEmail);
    System.out.println();

    /*
     * Seccion de prueba
     */
    boolean emailRegistered = userService.emailIsRegistered(nonexistentEmail);

    System.out.println("Resultado esperado: " + false);
    System.out.println("* Valor obtenido: " + emailRegistered);

    assertFalse(emailRegistered);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testThreeEmailIsRegistered() {
    System.out.println("*************************** Prueba tres del metodo emailIsRegistered ***************************");
    System.out.println("- En esta prueba se invoca al metodo emailIsRegistered de la clase UserServieceBean con el valor");
    System.out.println("null como argumento. Este metodo retorna true si un correo electronico esta registrado en la base");
    System.out.println("de datos subyacente, y false en caso contrario.");
    System.out.println();
    System.out.println("En este caso, el metodo emailIsRegistered retorna el valor booleano false porque invocarlo con el");
    System.out.println("valor null como argumento es como invocarlo con un correo electronico que NO esta registrado en la");
    System.out.println("base de datos subyacente.");
    System.out.println();

    /*
     * Seccion de prueba
     */
    boolean emailRegistered = userService.emailIsRegistered(null);

    System.out.println("Resultado esperado: " + false);
    System.out.println("* Valor obtenido: " + emailRegistered);

    assertFalse(emailRegistered);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testOneIsActive() {
    System.out.println("************************** Prueba uno del metodo isActive **************************");
    System.out.println("- En esta prueba se utiliza un usuario registrado en la base de datos subyacente, el");
    System.out.println("cual, esta activo. El metodo isActive de la clase UserServiceBean retorna true si un");
    System.out.println("usuario esta activo, y false si un usuario NO esta activo o si NO esta registrado en");
    System.out.println("la base de datos subyacente.");
    System.out.println();
    System.out.println("En este caso, el metodo isActive retorna el valor booleano true.");
    System.out.println();

    /*
     * Se imprime por pantalla los datos de todos los
     * usuarios registrados en la base de datos subyacente
     */
    System.out.println("Usuarios registrados en la base de datos subyacente");
    printAllUsers();

    /*
     * Obtencion de un usuario registrado en la base
     * de datos subyacente
     */
    System.out.println("* Usuario de prueba");
    User givenUser = userService.find(2);
    printUserData(givenUser);

    /*
     * Seccion de prueba
     */
    boolean activeUser = userService.isActive(givenUser.getEmail());

    System.out.println("Resultado esperado: " + true);
    System.out.println("* Valor obtenido: " + activeUser);

    assertTrue(activeUser);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testTwoIsActive() {
    System.out.println("************************** Prueba dos del metodo isActive **************************");
    System.out.println("- En esta prueba se utiliza un usuario registrado en la base de datos subyacente, el");
    System.out.println("cual, NO esta activo. El metodo isActive de la clase UserServiceBean retorna true si un");
    System.out.println("usuario esta activo, y false si un usuario NO esta activo o si NO esta registrado en");
    System.out.println("la base de datos subyacente.");
    System.out.println();
    System.out.println("En este caso, el metodo isActive retorna el valor booleano false.");
    System.out.println();

    /*
     * Se imprime por pantalla los datos de todos los
     * usuarios registrados en la base de datos subyacente
     */
    System.out.println("Usuarios registrados en la base de datos subyacente");
    printAllUsers();

    /*
     * Obtencion de un usuario registrado en la base
     * de datos subyacente
     */
    System.out.println("* Usuario de prueba");
    User givenUser = userService.find(6);
    printUserData(givenUser);

    /*
     * Seccion de prueba
     */
    boolean activeUser = userService.isActive(givenUser.getEmail());

    System.out.println("Resultado esperado: " + false);
    System.out.println("* Valor obtenido: " + activeUser);

    assertFalse(activeUser);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testThreeIsActive() {
    System.out.println("************************** Prueba tres del metodo isActive **************************");
    System.out.println("- En esta prueba se utiliza un usuario NO registrado en la base de datos subyacente.");
    System.out.println("El metodo isActive de la clase UserServiceBean retorna true si un usuario esta activo,");
    System.out.println("y false si un usuario NO esta activo o si NO esta registrado en la base de datos subyacente.");
    System.out.println();
    System.out.println("En este caso, el metodo isActive retorna el valor booleano false.");
    System.out.println();

    /*
     * Se imprime por pantalla los datos de todos los
     * usuarios registrados en la base de datos subyacente
     */
    System.out.println("Usuarios registrados en la base de datos subyacente");
    printAllUsers();

    /*
     * Correo electronico inexistente de prueba
     */
    String nonexistentEmail = "ghostemail@eservice.com";
    System.out.println("* Correo electronico inexistente en la base de datos subyacente: " + nonexistentEmail);
    System.out.println();

    /*
     * Seccion de prueba
     */
    boolean activeUser = userService.isActive(nonexistentEmail);

    System.out.println("Resultado esperado: " + false);
    System.out.println("* Valor obtenido: " + activeUser);

    assertFalse(activeUser);

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @Test
  public void testFourIsActive() {
    System.out.println("************************** Prueba cuatro del metodo isActive **************************");
    System.out.println("- En esta prueba se invoca al metodo isActive de la clase UserServiceBean con el valor");
    System.out.println("null como argumento. Este metodo retorna true si un usuario esta activo, y false si un");
    System.out.println("usuario NO esta activo o si NO esta registrado en la base de datos subyacente.");
    System.out.println();
    System.out.println("En este caso, el metodo isActive retorna el valor booleano false porque invocarlo con el");
    System.out.println("valor null como argumento es como invocarlo con un usuario que NO esta registrado en la");
    System.out.println("base de datos subyacente.");
    System.out.println();

    /*
     * Seccion de prueba
     */
    boolean activeUser = userService.isActive(null);

    System.out.println("Resultado esperado: " + false);
    System.out.println("* Valor obtenido: " + activeUser);

    assertFalse(activeUser);
  }

  public void testActivateUser() {
    System.out.println("****************************** Prueba del metodo activateUser ******************************");
    System.out.println("- En esta prueba se persiste un usuario inactivo, el cual, luego sera activado mediante el");
    System.out.println("metodo activateUser de la clase UserServiceBean.");
    System.out.println();

    /*
     * Creacion de un usuario
     */
    User newUser = new User();
    newUser.setUsername("Peter");
    newUser.setPassword("Peter");
    newUser.setEmail("peter@eservice.com");

    /*
     * Persistencia del usuario creado
     */
    entityManager.getTransaction().begin();
    newUser = userService.create(newUser);
    entityManager.getTransaction().commit();

    /*
     * El dato persistido es agregado a una coleccion para
     * su posterior eliminacion de la base de datos
     * subyacente
     */
    users.add(newUser);

    /*
     * Impresion de los datos del usuario creado
     */
    System.out.println("* Usuario de prueba");
    printUserData(newUser);

    /*
     * Seccion de prueba
     */
    entityManager.getTransaction().begin();
    userService.activateUser(newUser.getEmail());
    entityManager.getTransaction().commit();

    /*
     * Borra el contexto de persistencia, lo que hace que
     * todas las entidades administradas se desvinculen.
     * 
     * Si no se borra el contexto de persistencia luego de
     * activar el usuario, lo que se obtiene es el usuario
     * con el atributo "active" en false. Es decir, se
     * obtiene un usuario que no tiene los cambios que se
     * le realizaron en la base de datos subyacente. Por lo
     * tanto, es necesario ejecutar esta instruccion para
     * obtener el usuario actualizado, debido a que cuando
     * se borra el contexto de persistencia y se obtiene
     * un dato de la base de datos subyacente, se obtiene
     * el dato actual.
     */
    entityManager.clear();

    newUser = userService.find(newUser.getId());

    System.out.println("Valor esperado luego de la activacion: " + true);
    System.out.println("* Valor obtenido luego de la activacion: " + newUser.getActive());

    assertTrue(newUser.getActive());

    System.out.println("* Prueba ejecutada satisfactoriamente *");
  }

  @AfterClass
  public static void postTest() {
    entityManager.getTransaction().begin();
    
    /*
     * Elimina de la base de datos subyacente los usuarios persistidos durante la
     * ejecucion de la prueba unitaria para dejarla en su estado original,
     * es decir, para dejarla en el estado en el que estaba antes de que se
     * persistieran los usuarios creados
     */
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
    System.out.println("Datos de un usuario");
    System.out.println("ID: " + givenUser.getId());
    System.out.println("Nombre de usuario: " + givenUser.getUsername());
    System.out.println("Contraseña: " + givenUser.getPassword());
    System.out.println("Correo electronico: " + givenUser.getEmail());
    System.out.println("Activo: " + givenUser.getActive());
    System.out.println("Permiso de super usuario (administrador): " + givenUser.getSuperuser());
    System.out.println();
  }

  /**
   * Imprime los datos de todos los usuarios registrados en
   * la base de datos subyacente
   */
  private void printAllUsers() {
    Collection<User> users = userService.findAll();

    for (User currentUser : users) {
      printUserData(currentUser);
    }

  }

}

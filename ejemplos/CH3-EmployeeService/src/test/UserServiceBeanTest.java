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

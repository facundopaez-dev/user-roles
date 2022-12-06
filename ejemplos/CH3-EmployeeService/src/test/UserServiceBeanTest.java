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

}

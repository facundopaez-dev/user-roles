import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass;

import model.Employee;
import stateless.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;

public class EmployeeServiceBeanTest {
  public static EmployeeService service;
  protected static EntityManager em;
  protected static EntityManagerFactory emf;

  @BeforeClass
  public static void preTest(){
    emf = Persistence.createEntityManagerFactory("EmployeeService");
    em = emf.createEntityManager();
    service = new EmployeeServiceBean();
    service.setEntityManager(em);
  }

  @Test
  public void setAndGetIdTest() {
    Employee emp = new Employee(1);
    emp.setName("Demián");
    emp.setSalary(1000);

    em.getTransaction().begin();
    service.createEmployee(emp);
    em.getTransaction().commit();
    emp = service.findEmployee(1);
    assertEquals(emp.getId(),1);
    emp = service.findEmployee(2);
    assertNull(emp);
    
    //Collection <Employee> emps = service.findAllEmployees();
    //for (Employee empX : emps) {
    //  System.out.println(empX.getId() + ": " + empX.getName());
    //}
  }

  @AfterClass
  public static void postTest() {
    // elimina todos los datos de Employee
    em.getTransaction().begin();
    service.removeEmployee(1);
    em.getTransaction().commit();

    // Cierra las conexiones
    em.close();
    emf.close();
  }
}

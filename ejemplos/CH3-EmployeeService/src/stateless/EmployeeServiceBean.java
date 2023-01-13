package stateless;

import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Employee;

@Stateless
public class EmployeeServiceBean {

  @PersistenceContext(unitName = "EmployeeService")
  protected EntityManager em;

  //@Resource(mappedName="destinationQueue") Queue destinationQueue;
  //@Resource(mappedName="factory") QueueConnectionFactory factory;

  public void setEntityManager(EntityManager emLocal) {
    em = emLocal;
  }

  public EntityManager getEntityManager() {
    return em;
  }

  // public Employee createEmployee(int id, String name, long salary) {
  //     Employee emp = new Employee(id);
  //     emp.setName(name);
  //     emp.setSalary(salary);
  //     getEntityManager().persist(emp);
  //     return emp;
  // }

  public Employee create(Employee emp) {
    getEntityManager().persist(emp);
    return emp;
  }

  /**
   * Elimina el empleado de un usuario dado
   * 
   * @param userId
   * @param employeeId
   * @return referencia a un ojeto de tipo Employee si el empleado
   * a eliminar pertenece al usuario con el ID dado, null en caso
   * contrario
   */
  public Employee remove(int userId, int employeeId) {
    Employee givenEmployee = find(userId, employeeId);

    if (givenEmployee != null) {
      getEntityManager().remove(givenEmployee);
      return givenEmployee;    
    }

    return null;
  }

  public Employee changeEmployeeSalary(int id, long newSalary) {
    Employee emp = findEmployee(id);
    if (emp != null) {
      emp.setSalary(newSalary);
    }
    return emp;
  }

  public Employee findEmployee(int id) {
    return getEntityManager().find(Employee.class, id);
  }

  /**
   * Retorna el empleado de un usuario
   * 
   * @param userId
   * @param employeeId
   * @return referencia a un objeto de tipo Employee que contiene
   * el ID de usuario y el ID de empleado dados
   */
  public Employee find(int userId, int employeeId) {
    Query query = getEntityManager().createQuery("SELECT e FROM Employee e WHERE (e.id = :employeeId AND e.user.id = :userId)");
    query.setParameter("employeeId", employeeId);
    query.setParameter("userId", userId);

    return (Employee) query.getSingleResult();
  }

  /**
   * Comprueba si un empleado pertenece a un usuario dado.
   * 
   * Retorna true si y solo si el empleado pertenece al usuario
   * dado.
   * 
   * @param userId
   * @param employeeId
   * @return true si se encuentra el empleado con el ID y el ID
   * de usuario provistos, false en caso contrario
   */
  public boolean checkUserOwnership(int userId, int employeeId) {
    boolean result = false;

    try {
      find(userId, employeeId);
      result = true;
    } catch (NoResultException e) {
      e.printStackTrace();
    }

    return result;
  }

  public Collection<Employee> findAll(int userId) {
    Query query = getEntityManager().createQuery("SELECT e FROM Employee e WHERE e.user.id = :userId");
    query.setParameter("userId", userId);

    return (Collection) query.getResultList();
  }
  
}

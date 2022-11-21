package stateless;

import java.util.Collection;

import javax.persistence.EntityManager;

import model.Employee;

public interface EmployeeService {
    public void setEntityManager(EntityManager em);
    // public Employee createEmployee(int id, String name, long salary);
    public Employee createEmployee(Employee emp);
    public Employee removeEmployee(int id);
    public Employee changeEmployeeSalary(int id, long newSalary);
    public Employee findEmployee(int id);
    public Collection<Employee> findAllEmployees();
}

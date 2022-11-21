package servlet;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import model.Employee;
import stateless.EmployeeService;

import java.util.Collection;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Path("/employees")
public class EmployeeRestServlet {

  // inject a reference to the EmployeeService slsb
  @EJB EmployeeService service;

  //mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

	@GET
  @Produces(MediaType.APPLICATION_JSON)
	public String findAllEmployees() throws IOException {
		Collection<Employee> emps = service.findAllEmployees();
    return mapper.writeValueAsString(emps);
	}

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String findEmployee(@PathParam("id") int id) throws IOException {
    Employee emp = service.findEmployee(id);
    return mapper.writeValueAsString(emp);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public String createEmployee(String json) throws IOException {
    Employee emp = mapper.readValue(json, Employee.class);
    emp = service.createEmployee(emp);
    return mapper.writeValueAsString(emp);
  }


  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String removeEmployee(@PathParam("id") int id) throws IOException {
    Employee emp = service.removeEmployee(id);
    return mapper.writeValueAsString(emp);
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String changeEmployeeSalary(@PathParam("id") int id, @QueryParam("salary") long newSalary) throws IOException {
      Employee emp = service.changeEmployeeSalary(id,newSalary);
      return mapper.writeValueAsString(emp);
  }
}

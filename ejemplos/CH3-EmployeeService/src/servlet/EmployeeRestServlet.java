package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import model.Employee;
import stateless.EmployeeService;
import stateless.SecretKeyServiceBean;
import utilJwt.AuthHeaderManager;
import utilJwt.JwtManager;

@Path("/employees")
public class EmployeeRestServlet {

  // inject a reference to the EmployeeService slsb
  @EJB EmployeeService service;
  @EJB SecretKeyServiceBean secretKeyService;

  //mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllEmployees(@Context HttpHeaders request) throws IOException {
    String authHeaderValue = AuthHeaderManager.getAuthHeaderValue(request);

    /*
     * Si el encabezado de autorizacion de la peticion HTTP dada,
     * NO esta presente en la misma, no existe el valor de dicho
     * encabezado. Por lo tanto, la aplicacion del lado servidor
     * devuelve el mensaje HTTP 400 (Bad Request).
     */
    if (!AuthHeaderManager.isPresent(authHeaderValue)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /*
     * Si el encabezado de autorizacion de la peticion HTTP dada,
     * esta presente en la misma, pero esta vacio, el valor de dicho
     * encabezado esta vacio. Por lo tanto, la aplicacion del lado
     * servidor devuelve el mensaje 400 (Bad Request).
     */
    if (AuthHeaderManager.isEmpty(authHeaderValue)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /*
     * Si el valor del encabezado de autorizacion de la peticion
     * dada, NO cumple con la convencion de JWT, la aplicacion del
     * lado servidor devuelve el mensaje HTTP 400 (Bad Request)
     */
    if (!AuthHeaderManager.checkJwtConvention(authHeaderValue)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    String jwt = AuthHeaderManager.getJwt(authHeaderValue);

    /*
     * Si el JWT obtenido del valor del encabezado de autorizacion
     * de la peticion HTTP dada, NO es valido por algun motivo, la
     * aplicacion del lado servidor devuelve el mensaje HTTP 401
     * (Unauthorized)
     */
    if (!JwtManager.validateJwt(jwt, secretKeyService.find().getValue())) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /*
     * Si el valor del encabezado de autorizacion de la peticion HTTP
     * dada, tiene un JWT valido, la aplicacion del lado servidor
     * devuelve el mensaje HTTP 200 (Ok) junto con una coleccion
     * que contiene los datos solicitados
     */
    return Response.status(Response.Status.OK).entity(service.findAllEmployees()).build();
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

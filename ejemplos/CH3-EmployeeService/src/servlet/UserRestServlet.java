package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import model.User;
import stateless.UserServiceBean;
import utilLogin.LoginResponse;
import utilLogin.LoginStatus;
import utilPermission.PermissionResponse;

@Path("/users")
public class UserRestServlet {

  @EJB
  UserServiceBean userService;

  //mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String findAll() throws IOException {
    Collection<User> users = userService.findAll();
    return mapper.writeValueAsString(users);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String find(@PathParam("id") int id) throws IOException {
    User givenUser = userService.find(id);
    return mapper.writeValueAsString(givenUser);
  }

  @GET
  @Path("/checkSuperuserPermission/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response checkSuperuserPermission(@PathParam("username") String username) throws IOException {
    /*
     * Si el usuario con el nombre de usuario provisto no existe (en otras palabras, no
     * esta registrado en el sistema, y, por ende, no esta registrado en la base de datos
     * subyacente), la aplicacion del lado servidor devuelve a la aplicacion del lado
     * cliente, el mensaje HTTP 400 (BAD REQUEST)
     */
    if (userService.findByUsername(username) == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /*
     * Si el usuario con el nombre de usuario provisto, no tiene el permiso
     * de super usuario (administrador), la aplicacion del lado servidor
     * devuelve a la aplicacion del lado cliente, el mensaje HTTP 403 (FORBIDDEN)
     * con el mensaje "Acceso no autorizado"
     */
    if (!userService.checkSuperuserPermission(username)) {
      return Response.status(Response.Status.FORBIDDEN).entity(new PermissionResponse()).build();
    }

    /*
     * Si el usuario con el nombre de usuario provisto, tiene el permiso
     * de super usuario (administrador), la aplicacion del lado servidor
     * devuelve a la aplicacion del lado cliente, el mensaje HTTP 200 (OK)
     */
    return Response.status(Response.Status.OK).build();
  }

}

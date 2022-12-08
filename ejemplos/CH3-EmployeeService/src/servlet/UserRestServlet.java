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
  @Path("/authentication/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  public String find(@PathParam("username") String username, @QueryParam("password") String password) throws IOException {
    User givenUser = userService.validate(username, password);
    return mapper.writeValueAsString(givenUser);
  }

  @POST
  @Path("/authenticationAdmin")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response authenticateAdmin(String json) throws IOException {
    /*
     * Para la autenticacion del administrador se deben hacer las siguientes comprobaciones:
     * 1. La inexistencia del usuario ingresado. Si el usuario ingresado no existe (debido a que no esta registrado en el sistema), el
     * servidor debe devolver como respuesta el mensaje HTTP 401 con el mensaje "Nombre de usuario o contraseña incorrectos".
     * 
     * 2. La autenticacion del usuario. Si el nombre de usuario y la contraseña no son iguales a los almacenados en la base de datos
     * subyacente, el servidor debe devolver como respuesta el mensaje HTTP 401 (UNAUTHORIZED) con el mensaje "Nombre de usuario o contraseña incorrectos".
     * La autenticacion es el proceso por el cual se confirma que algo (en este caso un usuario) es realmente quien dice ser.
     * 
     * 3. La posesion del permiso de super usuario (administrador). Si el usuario ingresado existe, es autentico y no tiene el permiso de
     * super usuario, el servidor debe devolver como respuesta el mensaje HTTP 403 (FORBIDDEN) con el mensaje "Acceso no autorizado".
     * 
     * Las primeras dos comprobaciones se realizan al hacer la autenticacion del usuario.
     */

    User givenUser = mapper.readValue(json, User.class);

    if (!userService.authenticate(givenUser.getUsername(), givenUser.getPassword())) {
      return Response.status(Response.Status.UNAUTHORIZED)
      .entity(new LoginResponse(LoginStatus.USERNAME_OR_PASSWORD_INCORRECT)).build();
    }

    /*
     * La comprobacion del permiso de super usuario (administrador) del usuario nunca
     * va a fallar en caso de que se ingrese un usuario inexistente, ya que primero
     * se realiza la autenticacion, la cual cubre el caso de que el usuario ingresado
     * no existe.
     * 
     * Por lo tanto, siempre y cuando se realice la autenticacion del usuario
     * y luego la comprobacion del permiso de super usuario, dicha comprobacion
     * nunca va a fallar en caso de que se ingrese un usuario inexistente.
     */
    if (!userService.checkSuperuserPermission(givenUser.getUsername())) {
      return Response.status(Response.Status.FORBIDDEN).entity(new LoginResponse(LoginStatus.UNAUTHORIZED_ACCESS)).build();
    }

    /*
     * Si el usuario ingresado es autentico y tiene el permiso de super super usuario
     * (administrador), el servidor devuelve el mensaje HTTP 200 (OK) junto con el
     * usuario recuperado de la base de datos
     */
    return Response.status(Response.Status.OK)
    .entity(new LoginResponse(userService.findByUsername(givenUser.getUsername())))
    .build();
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

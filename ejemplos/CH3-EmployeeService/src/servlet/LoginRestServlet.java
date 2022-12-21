package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import model.User;
import model.SecretKey;
import stateless.SecretKeyServiceBean;
import stateless.UserServiceBean;
import utilJwt.JwtManager;
import utilJwt.Token;
import utilLogin.LoginResponse;
import utilLogin.LoginStatus;

@Path("/auth")
public class LoginRestServlet {

  @EJB
  UserServiceBean userService;

  @EJB
  SecretKeyServiceBean secretKeyService;

  //mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

  @POST
  @Path("/user")
  @Produces(MediaType.APPLICATION_JSON)
  public Response authenticateUser(String json) throws IOException {
    /*
     * Para la autenticacion del usuario se deben hacer las siguientes comprobaciones:
     * 1. La inexistencia del usuario ingresado. Si el usuario ingresado no existe (debido a que no esta registrado en el sistema), el
     * servidor debe devolver como respuesta el mensaje HTTP 401 con el mensaje "Nombre de usuario o contraseña incorrectos".
     *
     * 2. La autenticacion del usuario. Si el nombre de usuario y la contraseña no son iguales a los almacenados en la base de datos
     * subyacente, el servidor debe devolver como respuesta el mensaje HTTP 401 (UNAUTHORIZED) con el mensaje "Nombre de usuario o contraseña incorrectos".
     * La autenticacion es el proceso por el cual se confirma que algo (en este caso un usuario) es realmente quien dice ser.
     *
     * Estas dos comprobaciones se realizan al hacer la autenticacion del usuario.
     */

    User givenUser = mapper.readValue(json, User.class);

    if (!userService.authenticate(givenUser.getUsername(), givenUser.getPassword())) {
      return Response.status(Response.Status.UNAUTHORIZED)
      .entity(new LoginResponse(LoginStatus.USERNAME_OR_PASSWORD_INCORRECT)).build();
    }

    /*
     * Recupera de la base de datos subyacente la unica clave
     * secreta que hay para generar un JWT
     */
    SecretKey secretKey = secretKeyService.find();

    /*
     * Si el flujo de ejecucion de este metodo llega a este punto, es
     * debido a que el usuario que inicia sesion es autentico, por lo
     * tanto, se lo recupera de la base de datos subyacente para usar
     * su ID y su permiso en un JWT
     */
    givenUser = userService.findByUsername(givenUser.getUsername());

    /*
     * Si el usuario es autentico, el servidor devuelve el mensaje HTTP 200 (OK)
     * junto con un JWT que tiene el ID y el permiso del usuario, una fecha de
     * emision y una fecha de expiracion
     */
    return Response.status(Response.Status.OK)
    .entity(new Token(JwtManager.createJwt(givenUser.getId(), givenUser.getSuperuser(), secretKey.getValue())))
    .build();
  }

  @POST
  @Path("/admin")
  @Produces(MediaType.APPLICATION_JSON)
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
     * Recupera de la base de datos subyacente la unica clave
     * secreta que hay para generar un JWT
     */
    SecretKey secretKey = secretKeyService.find();

    /*
     * Si el flujo de ejecucion de este metodo llega a este punto, es
     * debido a que el usuario que inicia sesion es autentico y tiene
     * el permiso de super usuario (administrador), por lo tanto, se
     * lo recupera de la base de datos subyacente para usar su ID y su
     * permiso en un JWT
     */
    givenUser = userService.findByUsername(givenUser.getUsername());

    /*
     * Si el usuario es autentico y tiene el permiso de super usuario (administrador),
     * el servidor devuelve el mensaje HTTP 200 (OK) junto con un JWT que tiene el ID
     * y el permiso del usuario, una fecha de emision y una fecha de expiracion
     */
    return Response.status(Response.Status.OK)
    .entity(new Token(JwtManager.createJwt(givenUser.getId(), givenUser.getSuperuser(), secretKey.getValue())))
    .build();
  }

}

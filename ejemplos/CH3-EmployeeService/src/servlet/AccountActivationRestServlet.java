package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import stateless.UserServiceBean;
import util.ReasonSatisfaction;
import util.SatisfactoryResponse;

@Path("/activateAccount")
public class AccountActivationRestServlet {

  @EJB UserServiceBean userService;
  ObjectMapper mapper = new ObjectMapper();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response activateUser(@QueryParam("email") String email) throws IOException {

    /*
     * Si la direccion de correo electronico del enlace de activacion
     * NO esta registrada en la base de datos subyacente, la aplicacion
     * del lado servidor retorna el mensaje HTTP 400 (Bad request)
     */
    if (!userService.emailIsRegistered(email)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /*
     * Si el usuario que se registro en la aplicacion, activo
     * su cuenta y quiere activarla nuevamente, la aplicacion
     * del lado servidor devuelve el mensaje HTTP 400 (Bad
     * request).
     * 
     * El metodo isActive de la clase UserServiceBean contempla
     * el caso en el que el usuario que quiere activar su cuenta,
     * NO existe. Si el usuario que se quiere activar NO esta
     * registrado en la base de datos subyacente, la aplicacion
     * del lado servidor retorna el mensaje HTTP 400 (Bad request).
     */
    if (userService.isActive(email)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /*
     * Si el flujo de ejecucion llego a este punto, es debido a
     * que hay una cuenta de usuario registrada e inactiva en la
     * base de datos subyacente. Por lo tanto, se la debe activar
     * mediante esta instruccion.
     * 
     * Si un usuario registrado no activa su cuenta, no la podra
     * utilizar para iniciar sesion en la aplicacion.
     */
    userService.activateUser(email);

    /*
     * Si la cuenta de un usuario es activada, la aplicacion del
     * lado servidor retorna el mensaje HTTP 200 (Ok) junto con el
     * mensaje "Cuenta activada"
     */
    return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(new SatisfactoryResponse(ReasonSatisfaction.ACCOUNT_ACTIVATED))).build();
  }

}

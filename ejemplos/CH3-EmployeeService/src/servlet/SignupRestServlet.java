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
import stateless.UserServiceBean;
import util.Email;
import util.ErrorResponse;
import util.ReasonError;
import util.ReasonSatisfaction;
import util.RequestManager;
import util.SatisfactoryResponse;

@Path("/signup")
public class SignupRestServlet {

  @EJB UserServiceBean userService;

  // mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Response signup(String json) throws IOException {
    User givenUser = mapper.readValue(json, User.class);

    /*
     * Si la direccion de correo electronico ingresada por el usuario
     * en el formulario de registro, esta registrada en la base de
     * datos subyacente, la aplicacion del lado servidor retorna el
     * mensaje HTTP 400 (Bad request) junto con el mensaje "Correo
     * electronico ya utilizado, elija otro" y no se realiza el
     * registro solicitado
     */
    if (userService.emailIsRegistered(givenUser.getEmail())) {
      return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(ReasonError.EMAIL_ALREADY_USED)).build();
    }

    /*
     * Se registra (persiste) el usuario en la base de datos
     * subyacente, debido a que su direccion de correo electronico
     * no esta registrada en dicha base de datos
     */
    userService.create(givenUser);

    /*
     * Se envia un correo electronico de confirmacion de registro
     * al correo electronico del usuario para que este active su
     * cuenta, ya que de NO activarla no podra iniciar sesion con
     * ella en la aplicacion
     */
    Email.sendConfirmationEmail(givenUser.getEmail());

    /*
     * Al persistir (registrar) al usuario en la base de datos
     * subyacente, la aplicacion del lado servidor retorna el
     * mensaje HTTP 200 (Ok) junto con el mensaje "Usuario
     * registrado, revise su casilla de correo electrónico
     * para activar su cuenta"
     */
    return Response.status(Response.Status.OK).entity(new SatisfactoryResponse(ReasonSatisfaction.SUCCESSFULLY_REGISTERED_USER)).build();
  }

}

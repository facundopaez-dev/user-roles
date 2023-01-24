package servlet;

import java.io.IOException;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.SecretKeyServiceBean;
import util.RequestManager;

/**
 * ExpirationRestServlet es la clase que se utiliza para que
 * la aplicacion del lado del navegador web compruebe en los
 * controllers (Home.js y AdminHome.js) de las paginas web que no
 * realizan peticiones HTTP, si el JWT del usuario que tiene
 * una sesion abierta, expiro o no
 */
@Path("/expiration")
public class ExpirationRestServlet {

  @EJB SecretKeyServiceBean secretKeyService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response checkExpiration(@Context HttpHeaders request) throws IOException {
    return RequestManager.validateAuthHeader(request, secretKeyService.find());
  }

}

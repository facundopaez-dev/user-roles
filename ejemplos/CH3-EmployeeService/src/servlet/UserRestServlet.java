package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import model.User;
import stateless.SecretKeyServiceBean;
import stateless.UserServiceBean;
import util.RequestManager;
import utilLogin.LoginResponse;
import utilLogin.LoginStatus;
import utilPermission.PermissionResponse;

@Path("/users")
public class UserRestServlet {

  @EJB UserServiceBean service;
  @EJB SecretKeyServiceBean secretKeyService;

  //mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAll(@Context HttpHeaders request) throws IOException {
    Response givenResponse = RequestManager.validateAuthHeader(request, secretKeyService.find());

    /*
     * Si el estado de la respuesta obtenida de validar el
     * encabezado de autorizacion de una peticion HTTP NO
     * es ACCEPTED, se devuelve el estado de error de la misma.
     * 
     * Que el estado de la respuesta obtenida de validar el
     * encabezado de autorizacion de una peticion HTTP sea
     * ACCEPTED, significa que la peticion es valida,
     * debido a que el encabezado de autorizacion de la misma
     * cumple las siguientes condiciones:
     * - Esta presente.
     * - No esta vacio.
     * - Cumple con la convencion de JWT.
     * - Contiene un JWT valido.
     */
    if (!RequestManager.isAccepted(givenResponse)) {
      return givenResponse;
    }

    /*
     * Si el valor del encabezado de autorizacion de la peticion HTTP
     * dada, tiene un JWT valido, la aplicacion del lado servidor
     * devuelve el mensaje HTTP 200 (Ok) junto con los datos solicitados
     * por el cliente
     */
    return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(service.findAll())).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response find(@Context HttpHeaders request, @PathParam("id") int id) throws IOException {
    Response givenResponse = RequestManager.validateAuthHeader(request, secretKeyService.find());

    /*
     * Si el estado de la respuesta obtenida de validar el
     * encabezado de autorizacion de una peticion HTTP NO
     * es ACCEPTED, se devuelve el estado de error de la misma.
     * 
     * Que el estado de la respuesta obtenida de validar el
     * encabezado de autorizacion de una peticion HTTP sea
     * ACCEPTED, significa que la peticion es valida,
     * debido a que el encabezado de autorizacion de la misma
     * cumple las siguientes condiciones:
     * - Esta presente.
     * - No esta vacio.
     * - Cumple con la convencion de JWT.
     * - Contiene un JWT valido.
     */
    if (!RequestManager.isAccepted(givenResponse)) {
      return givenResponse;
    }

    /*
     * Si el valor del encabezado de autorizacion de la peticion HTTP
     * dada, tiene un JWT valido, la aplicacion del lado servidor
     * devuelve el mensaje HTTP 200 (Ok) junto con los datos solicitados
     * por el cliente
     */
    return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(service.find(id))).build();
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
    if (service.findByUsername(username) == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /*
     * Si el usuario con el nombre de usuario provisto, no tiene el permiso
     * de super usuario (administrador), la aplicacion del lado servidor
     * devuelve a la aplicacion del lado cliente, el mensaje HTTP 403 (FORBIDDEN)
     * con el mensaje "Acceso no autorizado"
     */
    if (!service.checkSuperuserPermission(username)) {
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

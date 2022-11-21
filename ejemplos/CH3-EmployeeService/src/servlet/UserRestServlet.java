package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import model.User;
import stateless.UserServiceBean;

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

}

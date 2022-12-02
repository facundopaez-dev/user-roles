package servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import model.Crop;
import stateless.CropServiceBean;

@Path("/crop")
public class CropRestServlet {

  // inject a reference to the CropServiceBean slsb
  @EJB
  CropServiceBean service;

  // mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String findAll() throws IOException {
    Collection<Crop> crops = service.findAll();
    return mapper.writeValueAsString(crops);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String find(@PathParam("id") int id) throws IOException {
    Crop crop = service.find(id);
    return mapper.writeValueAsString(crop);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public String create(String json) throws IOException {
    Crop newCrop = mapper.readValue(json, Crop.class);
    newCrop = service.create(newCrop);
    return mapper.writeValueAsString(newCrop);
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String remove(@PathParam("id") int id) throws IOException {
    Crop givenCrop = service.remove(id);
    return mapper.writeValueAsString(givenCrop);
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String modify(@PathParam("id") int id, String json) throws IOException {
    Crop modifiedCrop = mapper.readValue(json, Crop.class);
    modifiedCrop = service.modify(id, modifiedCrop);
    return mapper.writeValueAsString(modifiedCrop);
  }

}

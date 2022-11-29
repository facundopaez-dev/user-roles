package servlet;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import model.Parcel;

import stateless.ParcelServiceBean;

import java.util.Collection;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Path("/parcel")
public class ParcelRestServlet {

  // inject a reference to the ParcelServiceBean slsb
  @EJB ParcelServiceBean service;

  //mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String findAll() throws IOException {
    Collection<Parcel> parcels = service.findAll();
    return mapper.writeValueAsString(parcels);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String find(@PathParam("id") int id) throws IOException {
    Parcel parcel = service.find(id);
    return mapper.writeValueAsString(parcel);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public String create(String json) throws IOException {
    Parcel newParcel = mapper.readValue(json, Parcel.class);

    /*
     * Carga la nueva parcela con la fecha actual, la
     * cual representa la fecha de registro de una
     * parcela nueva
     */
    newParcel.setRegistrationDate(Calendar.getInstance());

    newParcel = service.create(newParcel);
    return mapper.writeValueAsString(newParcel);
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String remove(@PathParam("id") int id) throws IOException {
    Parcel parcel = service.remove(id);
    return mapper.writeValueAsString(parcel);
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String modify(@PathParam("id") int id, String json) throws IOException {
    Parcel modifiedParcel = mapper.readValue(json, Parcel.class);
    return mapper.writeValueAsString(service.modify(modifiedParcel.getId(), modifiedParcel));
  }

}

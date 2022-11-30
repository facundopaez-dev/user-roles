package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.Collection;

import javax.ejb.EJB;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import model.PlantingRecord;
import model.Parcel;

import stateless.PlantingRecordServiceBean;

@Path("/plantingRecord")
public class PlantingRecordRestServlet {

  // inject a reference to the PlantingRecordServiceBean slsb
  @EJB PlantingRecordServiceBean service;

  // mapea lista de pojo a JSON
  ObjectMapper mapper = new ObjectMapper();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String findAll() throws IOException {
    Collection<PlantingRecord> plantingRecords = service.findAll();
    return mapper.writeValueAsString(plantingRecords);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String find(@PathParam("id") int id) throws IOException {
    PlantingRecord plantingRecord = service.find(id);
    return mapper.writeValueAsString(plantingRecord);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public String create(String json) throws IOException {
    PlantingRecord newPlantingRecord = mapper.readValue(json, PlantingRecord.class);
    return mapper.writeValueAsString(service.create(newPlantingRecord));
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String remove(@PathParam("id") int id) throws IOException {
    PlantingRecord givenPlantingRecord = service.remove(id);
    return mapper.writeValueAsString(givenPlantingRecord);
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String modify(@PathParam("id") int id, String json) throws IOException  {
    PlantingRecord modifiedPlantingRecord = mapper.readValue(json, PlantingRecord.class);
    return mapper.writeValueAsString(service.modify(id, modifiedPlantingRecord));
  }

}

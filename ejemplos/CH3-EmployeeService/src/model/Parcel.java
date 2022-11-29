package model;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Parcel {

  /*
   * Instance variables
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "HECTARE", nullable = false)
  private double hectare;

  @Column(name = "LATITUDE", nullable = false)
  private double latitude; // en grados decimales

  @Column(name = "LONGITUDE", nullable = false)
  private double longitude; // en grados decimales

  @Column(name = "REGISTRATION_DATE", nullable = false)
  @Temporal(TemporalType.DATE)
  private Calendar registrationDate;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  public Parcel() {}

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getHectare() {
    return hectare;
  }

  public void setHectare(double hectare) {
    this.hectare = hectare;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public Calendar getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Calendar registrationDate) {
    this.registrationDate = registrationDate;
  }

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public String toString() {
    return String.format(
      "ID: %d\nNombre: %s\nHectárea: %f\nLatitud: %f\nLongitud: %f\nActiva: %b\n",
      id,
      name,
      hectare,
      latitude,
      longitude,
      active
    );
  }

}

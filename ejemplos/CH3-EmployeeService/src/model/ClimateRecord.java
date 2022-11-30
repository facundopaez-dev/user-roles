package model;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import util.UtilDate;

@Entity
@Table(name = "CLIMATE_RECORD", uniqueConstraints = {@UniqueConstraint(columnNames = { "DATE", "FK_PARCEL" })})
public class ClimateRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  /*
   * Fecha en la cual los datos climaticos
   * en base a unas coordenadas geograficas
   * han sido solicitados
   */
  @Column(name = "DATE", nullable = false)
  @Temporal(TemporalType.DATE)
  private Calendar date;

  @Column(name = "TEMP_MIN", nullable = false)
  private double temperatureMin;

  @Column(name = "TEMP_MAX", nullable = false)
  private double temperatureMax;

  @ManyToOne
  @JoinColumn(name = "FK_PARCEL", nullable = false)
  private Parcel parcel;

  // Constructor method
  public ClimateRecord() {}

  /**
   * Returns value of id
   * @return
   */
  public int getId() {
    return id;
  }

  /**
   * Returns value of date
   * @return
   */
  public Calendar getDate() {
    return date;
  }

  /**
   * Sets new value of date
   * @param
   */
  public void setDate(Calendar date) {
    this.date = date;
  }

  /**
   * Returns value of temperatureMin
   * @return
   */
  public Double getTemperatureMin() {
    return temperatureMin;
  }

  /**
   * Sets new value of temperatureMin
   * @param
   */
  public void setTemperatureMin(Double temperatureMin) {
    this.temperatureMin = temperatureMin;
  }

  /**
   * Returns value of temperatureMax
   * @return
   */
  public Double getTemperatureMax() {
    return temperatureMax;
  }

  /**
   * Sets new value of temperatureMax
   * @param
   */
  public void setTemperatureMax(Double temperatureMax) {
    this.temperatureMax = temperatureMax;
  }

  /**
   * Returns value of parcel
   * @return
   */
  public Parcel getParcel() {
    return parcel;
  }

  /**
   * Sets new value of parcel
   * @param
   */
  public void setParcel(Parcel parcel) {
    this.parcel = parcel;
  }

  @Override
  public String toString() {
    return String.format(
      "ID: %d\nFecha: %s\nTemperatura mínima: %f (°C)\nTemperatura máxima: %f (°C)\nParcela: %s\n",
      id,
      UtilDate.formatDate(date),
      temperatureMin,
      temperatureMax,
      parcel.getName()
    );
  }

}
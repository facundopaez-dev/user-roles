/*
 * Esta clase representa el registro historico
 * de la entidad parcela
 */

package model;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.UtilDate;

@Entity
@Table(name = "PLANTING_RECORD")
public class PlantingRecord {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "SEED_DATE", nullable = false)
  @Temporal(TemporalType.DATE)
  private Calendar seedDate;

  @Column(name = "HARVEST_DATE", nullable = false)
  @Temporal(TemporalType.DATE)
  private Calendar harvestDate;

  @ManyToOne
  @JoinColumn(name = "FK_PARCEL", nullable = false)
  private Parcel parcel;

  // Constructor method
  public PlantingRecord() {}

  public int getId() {
    return this.id;
  }

  public Calendar getSeedDate() {
    return seedDate;
  }

  public void setSeedDate(Calendar seedDate) {
    this.seedDate = seedDate;
  }

  public Calendar getHarvestDate() {
    return harvestDate;
  }

  public void setHarvestDate(Calendar harvestDate) {
    this.harvestDate = harvestDate;
  }

  public Parcel getParcel() {
    return parcel;
  }

  public void setParcel(Parcel parcel) {
    this.parcel = parcel;
  }

  @Override
  public String toString() {
    return String.format(
      "ID: %d\nFecha de siembra: %s\nFecha de cosecha: %s\nParcela: %s\n",
      id,
      UtilDate.formatDate(seedDate),
      UtilDate.formatDate(harvestDate),
      parcel.getName()
    );
  }
  
}
package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Crop {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "KC")
  private double cropCoefficient;

  @Column(name = "LIFE_CYCLE")
  private int lifeCycle;

  // Constructor method
  public Crop() {}

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getCropCoefficient() {
    return cropCoefficient;
  }

  public void setCropCoefficient(double cropCoefficient) {
    this.cropCoefficient = cropCoefficient;
  }

  public int getLifeCycle() {
    return lifeCycle;
  }

  public void setLifeCycle(int lifeCycle) {
    this.lifeCycle = lifeCycle;
  }

  @Override
  public String toString() {
    /*
     * Entre los integrantes del equipo de desarrollo se ha establecido
     * que las etapas inicial y desarrollo son una sola, y es por esto
     * que se suman los dias que duran ambas
     */
    return String.format(
      "Crop \nID: %d\nname: %s\ncrop coefficient: $.2f\nlife cycle: %d\n",
      id,
      name,
      cropCoefficient,
      lifeCycle
    );
  }
  
}

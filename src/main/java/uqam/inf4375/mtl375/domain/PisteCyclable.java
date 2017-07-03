package uqam.inf4375.mtl375.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PisteCyclable {
  private double id;
  private Geometry geometry;

    public PisteCyclable() {
    }

  public PisteCyclable(double id, Geometry geometry) {

    this.id = id;
    this.geometry = geometry;
  }

  @JsonProperty public Geometry getGeometry() { return geometry; }

  @Override public String toString() {
    return String.format(" : %.6f", id); // TODO: print avec geometry
  }
}

package uqam.inf4375.mtl375.resources;

public class PisteCyclable {
  private String type;
  private Property property;
  private Geometry geometry;

  public PisteCyclable(String type, Property property, Geometry geometry) {
    this.type = type;
    this.property = property;
    this.geometry = geometry;
  }

  @JsonProperty("type") public String getType() { return type; }
  @JsonProperty("properties") public Property getProperty() { return property; }
  @JsonProperty("geometry") public Geometry getGeometry() { return geometry; }

  @Override public String toString() {
    return String.format("%s", type); // TODO: print avec geometry et property
  }
}

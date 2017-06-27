package uqam.inf4375.mtl375.resources;

public class Geometry {
  private String type;
  private float[][] coordinates;

  public Geometry(String type, float[][] coordinates) {
    this.type = type;
    this.coordinates = coordinates;
  }

  @JsonProperty public String getType() { return type; }
  @JsonProperty public float[][] getCoordinates() { return coordinates; }

  @Override public String toString() {
    return String.format("%s", type); // TODO: print coordinates
  }
}

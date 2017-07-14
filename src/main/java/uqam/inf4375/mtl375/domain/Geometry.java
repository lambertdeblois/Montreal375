package uqam.inf4375.mtl375.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;

public class Geometry {
  private String type;
  private ArrayList<ArrayList<ArrayList<Double>>> coordinates;

  public Geometry(String type, ArrayList<ArrayList<ArrayList<Double>>> coordinates) {
    this.type = type;
    this.coordinates = coordinates;
  }

  @JsonProperty public String getType() { return type; }
  @JsonProperty public ArrayList<ArrayList<ArrayList<Double>>> getCoordinates() { return coordinates; }

  @Override public String toString() {
    return String.format("\n%s", type); // TODO: print coordinates
  }
}

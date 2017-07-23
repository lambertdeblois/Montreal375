package uqam.inf4375.mtl375.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PisteCyclable {

    private int id;
    private String geom;

    public PisteCyclable() {
    }

    public PisteCyclable(int id, String geom) {

        this.id = id;
        this.geom = geom;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getGeom() {
        return geom;
    }

    @Override
    public String toString() {
        return String.format("%d : %s", id, geom); // TODO: print avec geometry
    }
}

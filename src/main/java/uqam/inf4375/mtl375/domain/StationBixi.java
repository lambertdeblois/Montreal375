package uqam.inf4375.mtl375.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationBixi {
    private int id;
    private String name;
    private float lat;
    private float longueur;
    private int nbBikes;
    private int nbEmptyDocks;

    public StationBixi(int id, String name, float lat, float longueur, int nbBikes, int nbEmptyDocks) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.longueur = longueur;
        this.nbBikes = nbBikes;
        this.nbEmptyDocks = nbEmptyDocks;
    }

    public int getId() { return id; }
    @JsonProperty public String getName() { return name; }
    @JsonProperty public float getLat() { return lat; }
    @JsonProperty public float getLongueur() { return longueur; }
    @JsonProperty public int getNbBikes() { return nbBikes; }
    @JsonProperty public int getNbEmptyDocks() { return nbEmptyDocks; }


    @Override public String toString() {
        return String.format("%s : %d", name, id); // TODO: print plus beau
    }
}

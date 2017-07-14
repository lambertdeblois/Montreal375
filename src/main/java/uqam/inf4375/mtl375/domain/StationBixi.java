package uqam.inf4375.mtl375.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationBixi {
    private int id;
    private String name;
    private int nbBikes;
    private int nbEmptyDocks;
    private double lat;
    private double longueur;

    public StationBixi() {

    }

    public StationBixi(int id, String name, int nbBikes, int nbEmptyDocks, double lat, double longueur) {
        this.id = id;
        this.name = name;
        this.nbBikes = nbBikes;
        this.nbEmptyDocks = nbEmptyDocks;
        this.lat = lat;
        this.longueur = longueur;
    }

    @JsonProperty public int getId() { return id; }
    @JsonProperty public String getName() { return name; }
    @JsonProperty public int getNbBikes() { return nbBikes; }
    @JsonProperty public int getNbEmptyDocks() { return nbEmptyDocks; }
    @JsonProperty public double getLat() { return lat; }
    @JsonProperty public double getLongueur() { return longueur; }

    @Override public String toString() {
        return String.format("%d :(%.15f,%.15f)", id, lat, longueur); // TODO: print plus beau
    }
}

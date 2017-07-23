package uqam.inf4375.mtl375.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {

    private String name;
    private Double latitude;
    private Double longitude;

    public Place() {
    }

    public Place(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}

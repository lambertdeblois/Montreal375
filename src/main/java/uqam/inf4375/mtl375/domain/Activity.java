package uqam.inf4375.mtl375.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;

public class Activity {
    private int id;
    private String name;
    private String description;
    private String district;
    private Date[] dates;
    private Place place;


    public Activity() {      
    }

    public Activity(int id, String name, String description, String district, Date[] dates, Place place) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.district = district;
        this.dates = dates;
        this.place = place;
    }

    @JsonProperty public int getId() { return id; }
    @JsonProperty public String getName() { return name; }
    @JsonProperty public String getDescription() { return description; }
    @JsonProperty public String getDistrict() { return district; }
    @JsonProperty public Date[] getDates() { return dates; }
    @JsonProperty public Place getPlace() { return place; }

    @Override
    public String toString(){
        return String.format("%s : %s", name, district);
    }

}

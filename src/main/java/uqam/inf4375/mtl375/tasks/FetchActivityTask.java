package uqam.inf4375.mtl375.tasks;

import uqam.inf4375.mtl375.repositories.ActivityRepository;
import uqam.inf4375.mtl375.domain.Activity;
import uqam.inf4375.mtl375.domain.Place;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

import java.sql.*;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class FetchActivityTask {

    private static final Logger log = LoggerFactory.getLogger(FetchActivityTask.class);
    private static final String URL = "http://guillemette.org/uqam/inf4375-e2017/assets/programmation-parcs.json";

    @Autowired private ActivityRepository repository;

    //@Scheduled(cron="*/10 * * * * *")  // every 10 seconds.
    @Scheduled(cron = "0 0 0 * * 0") // at 0:00 every Sunday.
    public void execute(){
        Arrays.asList(new RestTemplate().getForObject(URL, FetchActivity[].class)).stream()
          .map(this::asActivity)
          .peek(a -> log.info(a.toString()))
          .forEach(repository::insert);
          ;
    }

    private Activity asActivity(FetchActivity a){
        Place place = new Place(a.place.nom, a.place.latitude, a.place.longitude);
        return new Activity(a.id, a.name, a.description, a.district, a.dates, place);
    }
}

class FetchActivity{
    @JsonProperty("id") int id;
    @JsonProperty("nom") String name;
    @JsonProperty("description") String description;
    @JsonProperty("arrondissement") String district;
    @JsonProperty("dates") Date[] dates;
    @JsonProperty("lieu") FetchPlace place;
}

class FetchPlace {
  @JsonProperty("nom") String nom;
  @JsonProperty("lat") Double latitude;
  @JsonProperty("lng") Double longitude;
}

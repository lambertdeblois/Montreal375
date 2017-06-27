package uqam.inf4375.mtl375.tasks;

import uqam.inf4375.mtl375.domain.*;

import java.util.*;
import java.util.stream.*;

import com.fasterxml.jackson.annotation.*;
import org.jsoup.*;
import org.slf4j.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.client.*;

@Component
public class FetchPistesCyclablesTask {

  private ArrayList<PisteCyclable> pistesCyclables;

  private static final Logger log = LoggerFactory.getLogger(FetchPistesCyclablesTask.class);
  private static final String URL = "http://donnees.ville.montreal.qc.ca/dataset/5ea29f40-1b5b-4f34-85b3-7c67088ff536/resource/0dc6612a-be66-406b-b2d9-59c9e1c65ebf/download/reseaucyclable2016dec2016.geojson";

  // @Scheduled(cron="0 0 1 */6 *") // à tous les 6 mois.
  @Scheduled(cron="*/10 * * * * ?") // à toutes les 5 secondes.
  public void execute() {
    // TODO: il faut modifier le fichier json pour avoir juste un array d'objets
    Arrays.asList(new RestTemplate().getForObject(URL, FetchPistesCyclables[].class)).stream()
      .map(this::asPisteCyclable)
      .peek(c -> log.info(c.toString()))
      ;
      pistesCyclables.toString();
  }

  private PisteCyclable asPisteCyclable(FetchPistesCyclables a){
        PisteCyclable b = new PisteCyclable(a.type, a.property, a.geometry);
        pistesCyclables.add(b);
        return b;
    }
}

class FetchPistesCyclables {
  @JsonProperty("type") String type;
  @JsonProperty("properties") Property property;
  @JsonProperty("geometry") Geometry geometry;
}

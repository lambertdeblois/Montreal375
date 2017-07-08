/*
 * Copyright 2017 Vincent Lafrenaye-Lirette <vi.lirette@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uqam.inf4375.mtl375.tasks;

import uqam.inf4375.mtl375.domain.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.sql.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uqam.inf4375.mtl375.repositories.ActivityRepository;

@Component
public class FetchActivityTask {

    private static final Logger log = LoggerFactory.getLogger(FetchActivityTask.class);
    private static final String URL = "http://guillemette.org/uqam/inf4375-e2017/assets/programmation-parcs.json";

    @Autowired private ActivityRepository repository;

    //@Scheduled(cron="*/10 * * * * ?")
    @Scheduled(cron = "*/5 * * * * ?") // à toutes les 5 secondes.
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

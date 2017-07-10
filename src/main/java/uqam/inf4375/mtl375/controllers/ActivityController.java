/*
 * Copyright 2017 Pivotal Software, Inc..
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
package uqam.inf4375.mtl375.controllers;

import java.util.*;

import uqam.inf4375.mtl375.domain.*;
import uqam.inf4375.mtl375.repositories.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
/**
 *
 * @author ladebloi
 */
public class ActivityController {

    @Autowired ActivityRepository repository;

    @RequestMapping(value="/activities/{id}", method=RequestMethod.DELETE)
    public Map<String, Object> deleteActivity(@PathVariable("id") int id) {
      Map<String, Object> response = new HashMap<String, Object>();
      Activity activity = repository.findById(id);
      if (activity != null){
        repository.delete(id);
        response.put("status code", 200);
        response.put("reponse", "deleted");
        response.put("activity", activity);
        return response;
      }
      response.put("status code", 404);
      response.put("reponse", "no activity");
      return response;
    }

    // @RequestMapping(value="/activities", method=RequestMethod.GET)
    // public Map<String, Object> getActivities(@RequestParam(value="rayon", required=false) Integer rayon,
    //                                          @RequestParam(value="lat", required=false) Double lat,
    //                                          @RequestParam(value="longueur", required=false) Double longueur,
    //
    //     if (rayon == null) rayon = 1000;
    //     if (lat == null) lat = 45.5087546;
    //     if (longueur == null) longueur = -73.5688033;
    //     if (nbBixi == null) nbBixi = 0;
    //     return getActivitiesParameters(rayon, lat, longueur, nbBixi);
    // }
    //
    // public Map<String, Object> getActivitiesParameters(int rayon, Double lat, Double longueur, int nbBixi){
    //     Map <String, Object> response = new HashMap<>();
    //
    //     if (rayon < 1) {
    //         response.put("satus code", 400);
    //         response.put("reponse", "rayon < 1");
    //         return response;
    //     }
    //     if (lat < -100 || lat > 100) {
    //         response.put("status code", 400);
    //         response.put("reponse", "lat invalide");
    //         return response;
    //     }
    //     if (longueur < -100 || longueur > 100) {
    //         response.put("status code", 400);
    //         response.put("reponse", "longueur invalide");
    //         return response;
    //     }
    //
    //     List<Acitivty> activities = repository.findWithParameters(rayon, lat, longueur, nbBixi);
    //
    //     if (activities.isEmpty()){
    //         response.put("status code", 404);
    //         response.put("reponse", "aucune activities trouvee");
    //         return response;
    //     }
    //
    //     response.put("status code", 200);
    //     response.put("reponse", "ok");
    //     response.put("stations", stations);
    //     return response;
    // }
}

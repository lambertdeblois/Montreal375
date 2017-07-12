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
import java.sql.Date;

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
    
    @RequestMapping(value="/activities/{id}", method=RequestMethod.PUT)
    public Map<String, Object> updateActivity(@RequestBody Activity activity, @PathVariable("id")int id){
        // valider le json qui rentre par le js avant dappeler la route
        Map<String, Object> response = new HashMap<>();
        if (repository.findById(activity.getId()) != null){
            repository.delete(activity.getId());
            repository.insert(activity);
            response.put("status code", 201);
            response.put("reponse", "activity updated");
            response.put("activity", activity);
        } else {
            response.put("status code", 400);
            response.put("reponse", "activity does not exist");
        }
        return response;
    }
    
    
    @RequestMapping(value="/activities", method=RequestMethod.POST)
    public Map<String, Object> addActivity(@RequestBody Activity activite){
        // valider le json qui rentre par le js avant dappeler la route
        Map<String, Object> response = new HashMap<>();
        if(repository.findById(activite.getId()) == null){
            repository.insert(activite);
            response.put("status code", 201);
            response.put("reponse", "activity created");

        } else {
            response.put("status code", 400);
            response.put("reponse", "activity already exist");
        }  
        return response;
    }

     @RequestMapping(value="/activities", method=RequestMethod.GET)
     public Map<String, Object> getActivities(@RequestParam(value="rayon", required=false) Integer rayon,
                                              @RequestParam(value="lat", required=false) Double lat,
                                              @RequestParam(value="longueur", required=false) Double longueur,
                                              @RequestParam(value="from", required=false)String from,
                                              @RequestParam(value="to", required=false)String to){
        Map<String, Object> response = new HashMap<String, Object>();
        List<Activity> activities;
        if (rayon != null && lat != null && longueur != null) {  //coord exist
            if (lat > -100 && lat < 100 && longueur > -100 && longueur < 100 && rayon > 0){ // si coord sont bonnes
                if (from != null && to != null) {  //dates exist
                    //coord, dates
                    Date dFrom = Date.valueOf(from);
                    Date dTo = Date.valueOf(to);
                    if (dFrom.compareTo(dTo) < 1) { //si dates sont bonnes
                        activities = repository.findWithCoordDates(dFrom, dTo, lat, longueur, rayon);
                        if (activities.isEmpty()){
                            response.put("status code", 404);
                            response.put("reponse", "aucune activité trouvée");
                            return response;
                        }                    
                    } else {
                        response.put("status code", 400);
                        response.put("message", "dates non valide");
                        return response;
                    }
                } else {
                   activities = repository.findWithCoord(lat, longueur, rayon);
                   if (activities.isEmpty()) {
                        response.put("status code", 404);
                        response.put("reponse", "aucune activité trouvée");
                        return response;
                   }
                }
            } else {  // si coord sont pas bonnes
                response.put("status code", 400);
                response.put("message", "coordonnées non valide");
                return response;
            }
        } else if (from != null && to != null) {  //dates exist
                Date dFrom = Date.valueOf(from);
                Date dTo = Date.valueOf(to);
                if (dFrom.compareTo(dTo) < 1) { //dates bonnes
                    activities = repository.findWithDates(dFrom, dTo);
                    if (activities.isEmpty()){
                        response.put("status code", 404);
                        response.put("reponse", "aucune activité trouvée");
                        return response;
                    }                    
                } else {
                    response.put("status code", 400);
                    response.put("message", "dates non valide");
                    return response;
                }
        } else { // coord et dates null
                activities = repository.findAll();
                if (activities.isEmpty()){
                    response.put("status code", 404);
                    response.put("reponse", "aucune activité trouvée");
                    return response;
                }
        }
        response.put("status code", 200);
        response.put("reponse", "ok");
        response.put("activity", activities);
        
        return response;
     }
    
     
}

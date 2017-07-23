package uqam.inf4375.mtl375.controllers;

import java.util.*;

import uqam.inf4375.mtl375.domain.*;
import uqam.inf4375.mtl375.repositories.*;

import java.sql.Date;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
public class ActivityController {

    @Autowired
    ActivityRepository repository;

    @RequestMapping(value = "/activities/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Activity> deleteActivity(@PathVariable("id") int id) {
        Activity activity = repository.findById(id);
        if (activity != null) {
            repository.delete(id);
            return new ResponseEntity<Activity>(activity, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/activities/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity activity, @PathVariable("id") int id) {
        // valider le json qui rentre par le js avant dappeler la route
        try {
            if (repository.findById(activity.getId()) != null) {
                repository.delete(activity.getId());
                repository.insert(activity);
                return new ResponseEntity<Activity>(activity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/activities/contenu", method = RequestMethod.GET)
    public ResponseEntity<List<Activity>> findByContenu(@RequestParam("term") String[] tsterms,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to) {
        List<Activity> activities;
        if (from != null && to != null) {
            Date dFrom = Date.valueOf(from);
            Date dTo = Date.valueOf(to);

            if (dFrom.compareTo(dTo) < 1) {
                activities = (tsterms.length == 0) ? repository.findAll() : repository.findByContenuWithDates(dFrom, dTo, tsterms);
                if (activities.isEmpty()) {
                    return new ResponseEntity<List<Activity>>(activities, HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            activities = (tsterms.length == 0) ? repository.findAll() : repository.findByContenu(tsterms);
            if (activities.isEmpty()) {
                return new ResponseEntity<List<Activity>>(activities, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<List<Activity>>(activities, HttpStatus.OK);
    }

    @RequestMapping(value = "/activities", method = RequestMethod.POST)
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activite) {
        try {
            if (repository.findById(activite.getId()) == null) {
                repository.insert(activite);
                return new ResponseEntity<Activity>(activite, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    public ResponseEntity<List<Activity>> getActivities(@RequestParam(value = "rayon", required = false) Integer rayon,
            @RequestParam(value = "lat", required = false) Double lat,
            @RequestParam(value = "longueur", required = false) Double longueur,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to) {
        List<Activity> activities;
        // 45.508931, -73.568568 pk
        if (rayon != null || lat != null || longueur != null) {  //coord exist
            if (rayon == null) {
                rayon = 5000;
            }
            if (lat == null) {
                lat = 45.508931;
            }
            if (longueur == null) {
                longueur = -73.568568;
            }
            if (lat > 40 && lat < 50 && longueur > -75 && longueur < -70 && rayon > 0) { // si coord sont bonnes
                if (from != null && to != null) {  //dates exist
                    //coord, dates
                    Date dFrom = Date.valueOf(from);
                    Date dTo = Date.valueOf(to);
                    if (dFrom.compareTo(dTo) < 1) { //si dates sont bonnes
                        activities = repository.findWithCoordDates(dFrom, dTo, lat, longueur, rayon);
                        if (activities.isEmpty()) {
                            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                        }
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                } else if (from != null || to != null) {
                    Date dFrom = new Date(new java.util.Date().getTime());
                    Date dTo = new Date(dFrom.getTime() + 24 * 60 * 60 * 1000);
                    activities = repository.findWithDates(dFrom, dTo);
                    if (activities.isEmpty()) {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                } else {
                    activities = repository.findWithCoord(lat, longueur, rayon);
                    if (activities.isEmpty()) {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                }
            } else {  // si coord sont pas bonnes
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else if (from != null && to != null) {  //dates exist
            Date dFrom = Date.valueOf(from);
            Date dTo = Date.valueOf(to);
            if (dFrom.compareTo(dTo) < 1) { //dates bonnes
                activities = repository.findWithDates(dFrom, dTo);
                if (activities.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else if (from != null || to != null) {
            Date dFrom = new Date(new java.util.Date().getTime());
            Date dTo = new Date(dFrom.getTime() + 24 * 60 * 60 * 1000);
            activities = repository.findWithDates(dFrom, dTo);
            if (activities.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else { // coord et dates null
            activities = repository.findAll();
            if (activities.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<List<Activity>>(activities, HttpStatus.OK);
    }

}

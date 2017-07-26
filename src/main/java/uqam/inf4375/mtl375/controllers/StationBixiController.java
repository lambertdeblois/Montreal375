package uqam.inf4375.mtl375.controllers;

import java.util.*;

import uqam.inf4375.mtl375.domain.*;
import uqam.inf4375.mtl375.repositories.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.dao.*;

@RestController
public class StationBixiController {

    @Autowired
    StationBixiRepository repository;

    /**
     * Get a specific StationBixi by its ID.
     *
     * ROUTE: GET /stationsBixi/{id}
     *
     * @return a JSON object containing the stationsBixi.
     */
    @RequestMapping(value = "/stationsBixi/{id}", method = RequestMethod.GET)
    public ResponseEntity<StationBixi> getStationById(@PathVariable("id") int id) {
        StationBixi station = null;
        try {
            station = repository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (station != null) {
            return new ResponseEntity<StationBixi>(station, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a list of stationsBixi matching the parameters.
     *
     * ROUTE: GET /stationsBixi
     *
     * @return a list of stationsBixi matching the parameters && a JSON object
     * containing the status.
     */
    @RequestMapping(value = "/stationsBixi", method = RequestMethod.GET)
    public ResponseEntity<List<StationBixi>> getStations(@RequestParam(value = "rayon", required = false) Integer rayon,
            @RequestParam(value = "lat", required = false) Double lat,
            @RequestParam(value = "longueur", required = false) Double longueur,
            @RequestParam(value = "nbBixi", required = false) Integer nbBixi) {
        if (rayon == null && lat == null && lat == null && longueur == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (rayon == null) {
            rayon = 1000;
        }
        if (lat == null) {
            lat = 45.5087546;
        }
        if (longueur == null) {
            longueur = -73.5688033;
        }
        if (nbBixi == null) {
            nbBixi = 0;
        }
        return getStationsParameters(rayon, lat, longueur, nbBixi);
    }

    /**
     * Returns a list of stationsBixi matching the parameters
     * @param rayon 
     * @param lat pour la geolocalisation de la station
     * @param longueur pour la geolocalisation de la station
     * @param nbBixi disponible a la station
     * @return a list of stationsBixi matching the parameters && a JSON object
     * containing the status.
     */
    public ResponseEntity<List<StationBixi>> getStationsParameters(int rayon, Double lat, Double longueur, int nbBixi) {
        if (rayon < 1 || lat < 40 || lat > 50 || longueur < -80 || longueur > -70 || nbBixi < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<StationBixi> stations = repository.findWithParameters(rayon, lat, longueur, nbBixi);

        if (stations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<StationBixi>>(stations, HttpStatus.OK);
    }
}

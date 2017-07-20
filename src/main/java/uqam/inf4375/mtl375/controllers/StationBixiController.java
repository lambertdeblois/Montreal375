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
import org.springframework.http.*;
import org.springframework.dao.*;


@RestController
/**
 *
 * @author ladebloi
 */
public class StationBixiController {

    @Autowired StationBixiRepository repository;

    @RequestMapping(value="/stationsBixi/{id}", method=RequestMethod.GET)
    public ResponseEntity<StationBixi> getStationById(@PathVariable("id") int id) {
        StationBixi station = null;
        try {
            station = repository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (station != null){
            return new ResponseEntity<StationBixi>(station, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value="/stationsBixi", method=RequestMethod.GET)
    public ResponseEntity<List<StationBixi>> getStations(@RequestParam(value="rayon", required=false) Integer rayon,
                                           @RequestParam(value="lat", required=false) Double lat,
                                           @RequestParam(value="longueur", required=false) Double longueur,
                                           @RequestParam(value="nbBixi", required=false) Integer nbBixi){

        if (rayon == null) rayon = 1000;
        if (lat == null) lat = 45.5087546;
        if (longueur == null) longueur = -73.5688033;
        if (nbBixi == null) nbBixi = 0;
        return getStationsParameters(rayon, lat, longueur, nbBixi);
    }

    public ResponseEntity<List<StationBixi>> getStationsParameters(int rayon, Double lat, Double longueur, int nbBixi){
        if (rayon < 1 || lat < 40 || lat > 50 || longueur < -80 || longueur > -70 || nbBixi < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<StationBixi> stations = repository.findWithParameters(rayon, lat, longueur, nbBixi);

        if (stations.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<StationBixi>>(stations, HttpStatus.OK);
    }
}

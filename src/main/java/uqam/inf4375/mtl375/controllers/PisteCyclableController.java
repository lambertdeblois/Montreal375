package uqam.inf4375.mtl375.controllers;

import java.util.*;

import uqam.inf4375.mtl375.domain.*;
import uqam.inf4375.mtl375.repositories.*;

import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;


@RestController
public class PisteCyclableController {

    @Autowired PisteCyclableRepository repository;

     @RequestMapping(value="/pistes", method=RequestMethod.GET)
     public ResponseEntity<List<PisteCyclable>> getPistes(@RequestParam(value="rayon", required=false) Integer rayon,
                                              @RequestParam(value="lat", required=false) Double lat,
                                              @RequestParam(value="longueur", required=false) Double longueur){
        List<PisteCyclable> pistes;
        if (rayon == null) rayon = 200;

        if (lat != null && longueur != null) {
            if (lat > 40 && lat < 50 && longueur > -75 && longueur < -70 && rayon > 0){ // si coord sont bonnes
                pistes = repository.findWithPoint(lat, longueur, rayon);
                if (pistes.isEmpty()){
                  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<PisteCyclable>>(pistes, HttpStatus.OK);
     }


}

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
import org.springframework.http.*;


@RestController
/**
 *
 * @author ladebloi
 */
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

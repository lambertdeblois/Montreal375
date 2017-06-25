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
package uqam.inf4375.mtl375.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author vincent
 */
public class Place {
    private String name;
    private double latitude;
    private double longitude;

    public Place() {
    }

    public Place(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @JsonProperty public String getName() { return name; }
    @JsonProperty public double getLatitude() { return latitude; }
    @JsonProperty public double getLongitude() { return longitude; }
    
    
}

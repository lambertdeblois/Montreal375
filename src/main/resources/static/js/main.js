 var mymap = L.map('carte').setView([-73.568568, 45.508931], 10);
 var baseURL = new URL(window.location.origin);
 var markers = new L.FeatureGroup();
 var gActivities = new L.FeatureGroup();

 var greenIcon = new L.Icon({
   iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
   shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
   iconSize: [25, 41],
   iconAnchor: [12, 41],
   popupAnchor: [1, -34],
   shadowSize: [41, 41]
 });

L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
  attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
  maxZoom: 18,
  id: 'mapbox.streets',
  accessToken: 'pk.eyJ1IjoiaW5mNDM3NW1hcCIsImEiOiJjajRlYng5dWswMXR5MzNsYTJ0Y3owcnoxIn0._-7Nq-cOcGkjK1Ti6ERasA'
}).addTo(mymap);


var markerStations = function(station){
    var marker = L.marker([station.lat, station.longueur], {icon: greenIcon}).addTo(mymap);
    marker.bindPopup('Nb bixis: ' + station.nbBikes + '<br>Nb Places: ' + station.nbEmptyDocks);
    markers.addLayer(marker);
}

var fetchStations = function (url) {
  fetch(url).then(function(resp) {
    return resp.json()
  }).then(function (data) {
    if (data !== null){
        data.map(markerStations);
        mymap.addLayer(markers);
    }
  })
}

function delMarkerStations() {
    mymap.removeLayer(markers);
    markers = new L.FeatureGroup();
}


function zoom(latitude, longitude) {
  mymap.setView(new L.LatLng(latitude, longitude), 15);
  delMarkerStations();
  fetchStations( new URL('/stationsBixi?rayon=500&lat='+latitude+'&longueur='+longitude, baseURL));
}


var renderActivity = function (activity) {
  var lat = activity.place.latitude;
  var lng = activity.place.longitude;
  if (lat === 0 || lng === 0){
    return '<li>' + activity.name + '</li>';
  } else {
    var marker = L.marker([lat, lng]).addTo(mymap);
    marker.bindPopup(activity.name + '<br>' + activity.description + "<br>" + activity.dates + "<br>" + activity.place.name);
    gActivities.addLayer(marker);
    return '<li> <a onclick="zoom(' + lat + ',' + lng + ')">' + activity.name +'</li>';
  }
}

var renderListeActivities = function (activities) {
  return '<ul>'+ activities.map(renderActivity).join('') +'</ul>';
}

var installerListeActivities = function (listeActivitiesHtml) {
  mymap.addLayer(gActivities);
  mymap.closePopup();
  document.getElementById('liste-activities').innerHTML = listeActivitiesHtml;
}


var fetchActivities = function (url) {
  mymap.removeLayer(gActivities);
  gActivities = new L.FeatureGroup();
  fetch(url).then(function(resp) {
    return resp.json()
  }).then(function (data) {
    if (data.length === 0) {
      mymap.setView(new L.LatLng(45.508931, -73.568568), 10);
      installerListeActivities("Aucune activite trouvee.")
    } else {
      installerListeActivities(renderListeActivities(data));
      mymap.closePopup();
      mymap.fitBounds(gActivities.getBounds());
    }
  })
}

var rechercher = function (terms) {
  var url = new URL('/activities/contenu', baseURL);
  terms.forEach(function (t) { url.searchParams.append('term', t) })

  fetchActivities(url)
}

var rechercherWithDates = function (terms, from, to) {
  var url = new URL('/activities/contenu', baseURL);
  terms.forEach(function (t) { url.searchParams.append('term', t); });
  url.searchParams.append('from', from);
  url.searchParams.append('to', to);
  fetchActivities(url);
}

var lierFormulaire = function () {
    var form = document.getElementById('search-form');
    var input = document.getElementById('search-input');
    var from = document.getElementById('from-input');
    var to = document.getElementById('to-input');
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      if (input.value === "") {
        installerListeActivities("Parametre de recherche invalides.");
      } else if (from.value !== "" && to.value !== "") {
        rechercherWithDates(input.value.split(/\s+/), from.value, to.value);
      } else {
        rechercher(input.value.split(/\s+/));
      }
      mymap.removeLayer(gActivities);
      gActivities = new L.FeatureGroup();

    })
}


document.addEventListener('DOMContentLoaded', function() {
    fetchActivities( new URL('/activities', baseURL));
    mymap.setView(new L.LatLng(45.508931, -73.568568), 10);
    lierFormulaire();
})



//https://github.com/github/fetch#read-this-first  pour faire un appel a une route (ajax) fetch
//https://github.com/Morriar/INF4375-http-tester   pour faire un appel a une route (ajax) fetch
//https://github.com/Morriar/INF4375-jsonschema/blob/master/exercice1.js   pour valider json schema

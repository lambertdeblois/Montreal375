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
    if (data.stations !== null){
        data.stations.map(markerStations);
        mymap.addLayer(markers);
    }
  })
}

function delMarkerStations() {
    mymap.removeLayer(markers);
    markers = new L.FeatureGroup();
}


function zoom(latitude, longitude) {
  mymap.setView(new L.LatLng(latitude, longitude), 13);
  delMarkerStations();
  fetchStations( new URL('/stationsBixi?rayon=500&lat='+latitude+'&longueur='+longitude, baseURL));
}


var renderActivity = function (activity) {
  var lat = activity.place.latitude;
  var lng = activity.place.longitude;
  var marker = L.marker([lat, lng]).addTo(mymap);
  marker.bindPopup(activity.name + '<br>' + activity.description + "<br>" + activity.dates + "<br>" + activity.place.name).openPopup();
  gActivities.addLayer(marker);
  if (lat === 0.0 || lng === 0.0){
    return '<li>' + activity.name + '</li>';
  } else {
  return '<li> <a onclick="zoom(' + lat + ',' + lng + ')">' + activity.name +'</li>';
  }
}

var renderListeActivities = function (activities) {
  return '<ul>'+ activities.map(renderActivity).join('') +'</ul>';
  mymap.addLayer(gActivities);
}

var installerListeActivities = function (listeActivitiesHtml) {
  document.getElementById('liste-activities').innerHTML = listeActivitiesHtml;
}


var fetchActivities = function (url) {
  mymap.removeLayer(gActivities);
  fetch(url).then(function(resp) {
    return resp.json()
  }).then(function (data) {
    installerListeActivities(renderListeActivities(data.activity))
  })
}


var lierFormulaire = function () {
    var form = document.getElementById('search-form');
    var input = document.getElementById('search-input');
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      rechercher(input.value.split(/\s+/));
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

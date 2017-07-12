var mymap = L.map('carte').setView([45.5086365, -73.5688923], 10);

L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
  attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
  maxZoom: 18,
  id: 'mapbox.streets',
  accessToken: 'pk.eyJ1IjoiaW5mNDM3NW1hcCIsImEiOiJjajRlYng5dWswMXR5MzNsYTJ0Y3owcnoxIn0._-7Nq-cOcGkjK1Ti6ERasA'
}).addTo(mymap);

var baseURL = new URL(window.location.origin);

function zoom(latitude, longitude) {
  mymap.panTo([latitude, longitude]);
  mymap.setZoom(13);
}


var renderActivity = function (activity) {
  var lat = activity.place.latitude;
  var lng = activity.place.longitude;
  var marker = L.marker([lat, lng]).addTo(mymap);
  marker.bindPopup(activity.name + '<br>' + activity.description + "<br>" + activity.dates + "<br>" + activity.place.name).openPopup();

  if (lat === 0.0 || lng === 0.0){
    return '<li>' + activity.name + '</li>';
  } else {
  return '<li> <a onclick="zoom(' + lat + ',' + lng + ')">' + activity.name +'</li>';
  }
}

var renderListeActivities = function (activities) {
  return '<ul>'+ activities.map(renderActivity).join('') +'</ul>';
}

var installerListeActivities = function (listeActivitiesHtml) {
  document.getElementById('liste-activities').innerHTML = listeActivitiesHtml;
}


var fetchActivities = function (url) {
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
    mymap.panTo([45.5086365, -73.5688923]);
    mymap.setZoom(10);
    lierFormulaire();
})



//https://github.com/github/fetch#read-this-first  pour faire un appel a une route (ajax) fetch
//https://github.com/Morriar/INF4375-http-tester   pour faire un appel a une route (ajax) fetch
//https://github.com/Morriar/INF4375-jsonschema/blob/master/exercice1.js   pour valider json schema

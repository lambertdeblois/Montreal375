drop table stationbixi;
create extension Postgis;
create table stationbixi (
    id int primary key,
    name text,
    nbBikes int,
    nbEmptyDocks int,
    lieu geography(POINT, 4326)
);

drop table pistecyclable;
drop table stationbixi;
drop table activities;
create extension Postgis;

create table pistecyclable (
    id int primary key
  , geom geometry(LINESTRING, 4326)
);


create table stationbixi (
    id int primary key
  , name text
  , nbBikes int
  , nbEmptyDocks int
  , lieu geography(POINT, 4326)
);


create table activities (
    id int primary key
  , name text
  , description text
  , district text
  , dates date[4]
  , nomPlace text
  , lieu geography(POINT, 4326)
);

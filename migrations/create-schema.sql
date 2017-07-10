create extension postgis;
drop table activities;

create table activities (
    id int primary key
  , name text
  , description text
  , district text
  , dates date[4]
  , nomPlace text
  , lieu geography(POINT, 4326)
);

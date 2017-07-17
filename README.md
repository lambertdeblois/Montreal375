# mtl375

## Prerequisites

- Java 1.8+
- Maven 3.0+
- PostgreSQL 9.4+ with PostGIS 2.0+

## Database migrations

1. Create the database mtl375 with `psql -U postgres -f migrations/create-database.sql` (password: `postgres`)

2. Create the tables with `psql -U postgres -f migrations/create-schema.sql mtl375` (password: `postgres`)

## Compilation and execution

Execute `mvn spring-boot:run` at the projet root

You can now go to [http://localhost:8080/](http://localhost:8080/) 


## Ce qui marche
A1, A3, A4, B4, C1, C2, C3, C4, D1, D2

5xp 5xp 5xp 2xp 5xp 2xp 5xp 5xp 5xp 10xp

49xp

## À faire
B1 (5xp): Faire le js qui prend les dates et qui fait appel à la recherche (SQL pour recherche déjà faite) et afficher la liste
B2 (5xp): Retracer les markers et recentrer la carte après une requête B1
B3 (5xp): Faire les formulaire pour update/create/delete.  Les APIS sont fait, ils faut faire le js qui fait les appels.  Updater la liste après chaque appel.

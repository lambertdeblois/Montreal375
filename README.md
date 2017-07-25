# mtl375

## Prerequisites

- Java 1.8+
- Maven 3.0+
- PostgreSQL 9.4+ with PostGIS 2.0+

## Database migrations

1. Create the database mtl375 with `psql -U postgres -f migrations/create-database.sql` (password: `postgres`)

2. Create the tables with `psql -U postgres -f migrations/create-schema.sql mtl375` (password: `postgres`)

## Compilation and execution

Execute `mvn spring-boot:run` at the project root.

You can now go to [http://localhost:8080/](http://localhost:8080/)

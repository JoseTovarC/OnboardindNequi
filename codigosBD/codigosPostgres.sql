podman run --name prueba -e POSTGRES_PASSWORD=aprendiendoNequi -p 5433:5432 -d postgres

podman exec -it prueba sh

psql -h localhost -p 5432 -U postgres



--CREATE DATABASE taller;

--USE taller;

CREATE TABLE usuarios (
     second_id  serial PRIMARY KEY,
     id INTEGER UNIQUE,
     firstname VARCHAR(50),
     lastname VARCHAR(50),
     email VARCHAR(100) UNIQUE,
     avatar VARCHAR(100) UNIQUE
 );

 CREATE TABLE person (
     id INTEGER PRIMARY KEY,
     nombre VARCHAR(50),
     documento INTEGER UNIQUE
 );

INSERT INTO public.person(id, nombre, documento) VALUES (12, 'José', 1193126480);

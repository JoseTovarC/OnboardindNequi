# Guia basica para ejecución de proyecto

## Requerimientos
Tener instalado un IDE que permita correr el mainApplication().
Tener podman corriendo en la maquina (en el caso de este proyecto, tambien funcionaria con Docker)




## Pasos a seguir

**Ejecutar en orden las siguientes líneas de codigo en consola:**

```shell

podman run -d --name redis_prueba  -p 6379:6379 redis

podman run --name prueba -e POSTGRES_PASSWORD=aprendiendoNequi -p 5433:5432 -d postgres

podman exec -it prueba sh

psql -h localhost -p 5432 -U postgres
```

```sql

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

```

**Si los contenedores de Redis y Postgres corren bien no habria ningun problema, pero en caso de que no,
una posibilidad es que los puertos del equipo se encuentren ocupados, asi que se sugiere cambiar el puerto 
externo(Local del PC) del contenedor que no este corriendo, por ejemplo: -p 6380:6379 (Local:Contenedor) y
despues de hacer esto prosigue cambiar los puertos en el archivo:**

applications\app-service\src\main\resources\application.yaml

**Cambiar y poner el puerto local asi:**

![img.png](codigosBD\GuideImages\Captura de pantalla 2023-08-10 143538.png)

![img_1.png](codigosBD\GuideImages\Captura de pantalla 2023-08-10 143803.png)
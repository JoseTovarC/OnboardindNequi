FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY app/build/lib/* build/lib/

COPY prueba/build/libs/prueba.jar build/

WORKDIR /prueba/build
ENTRYPOINT java -jar app.jar
# Usar una imagen base con JDK 8 y Gradle
FROM gradle:7.3.0-jdk11 AS build

# Establecer un directorio de trabajo
WORKDIR /app

# Copiar archivos de tu proyecto al directorio de trabajo
COPY . /app

# Ejecutar Gradle para construir el proyecto
RUN gradle clean build

FROM eclipse-temurin:17-jdk AS build

FROM eclipse-temurin:17-jre
ARG PORT
ENV PORT=${PORT}
COPY --from=build /app/prueba.jar .
RUN useradd runtime
USER runtime
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "prueba.jar" ]
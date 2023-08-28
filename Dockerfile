# Usar una imagen base con JDK 8 y Gradle
FROM gradle:8.0.2-jdk17 AS build

# Establecer un directorio de trabajo
WORKDIR /app

# Copiar archivos de tu proyecto al directorio de trabajo
COPY . /app
FROM eclipse-temurin:17-jdk AS build

FROM eclipse-temurin:17-jre
# Ejecutar Gradle para construir el proyecto
RUN ./gradlew clean build

ARG PORT
ENV PORT=${PORT}
COPY --from=build /app/build/libs/prueba.jar /app/prueba.jar
RUN useradd runtime
USER runtime
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "prueba.jar" ]
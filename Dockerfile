# Usar una imagen base con JDK 8 y Gradle
FROM gradle:8.0.2-jdk17 AS build

# Establecer un directorio de trabajo
WORKDIR /app

# Copiar archivos de tu proyecto al directorio de trabajo
COPY . /app

# Ejecutar Gradle para construir el proyecto
RUN gradle clean build

# Crear una nueva imagen basada en OpenJDK 8
FROM eclipse-temurin:11.0.12_7-jdk
CMD ["java", "-jar", "/opt/app/prueba.jar"]

# Exponer el puerto que utilizará la aplicación
EXPOSE 8080

# Copiar el archivo JAR construido desde la etapa anterior
COPY --from=build /app/build/libs/prueba.jar /app/prueba.jar

# Establecer el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/onboarding.jar"]
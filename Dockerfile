FROM eclipse-temurin:17-jdk AS build
COPY . /app
WORKDIR /app
RUN ./gradlew bootJar
RUN mv -f build/libs/*.jar prueba.jar

FROM eclipse-temurin:17-jre
ARG PORT
ENV PORT=${PORT}
COPY --from=build /app/prueba.jar .
RUN useradd runtime
USER runtime
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "prueba.jar" ]
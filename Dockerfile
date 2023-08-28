FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} prueba.jar
ENTRYPOINT ["java","-jar","/prueba.jar"]
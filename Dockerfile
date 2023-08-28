FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY app/build/lib/* build/lib/

COPY OnboardingNequi/build/libs/prueba.jar build/

WORKDIR /OnboardingNequi/build
ENTRYPOINT java -jar app.jar
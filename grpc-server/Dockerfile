#docker
FROM openjdk:17
WORKDIR /app
COPY /target/*.jar build/
EXPOSE 9090
WORKDIR /app/build
ENTRYPOINT java -jar grpc-server-1.0.0-jar-with-dependencies.jar
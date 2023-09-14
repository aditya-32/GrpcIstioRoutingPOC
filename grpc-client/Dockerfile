
#docker
FROM openjdk:17
WORKDIR /app
COPY target/grpc-client-1.0.0-jar-with-dependencies.jar build/
EXPOSE 8080
EXPOSE 9090
WORKDIR /app/build
ENTRYPOINT java -jar grpc-client-1.0.0-jar-with-dependencies.jar

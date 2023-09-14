package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.protobuf.services.HealthStatusManager;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GrpcServer {
    private static final ServerConfig config = ServerConfigReader.getServerConfig();

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("-------Starting Grpc Server------");
        var healthServiceManager = new HealthStatusManager();
        Server server = ServerBuilder.forPort(config.grpcPort())
                .addService(new GrpcServerCustomImpl())
                .addService(healthServiceManager.getHealthService())
                .build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("-------Shutting Down server------");
            server.shutdown();
            try {
                if (!server.awaitTermination(20, TimeUnit.SECONDS)) {
                    server.shutdownNow();
                }
                server.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println("Graceful shutdown interrupted, Forcing Shutdown");
                server.shutdownNow();
            }
        }));
        healthServiceManager.setStatus("", HealthCheckResponse.ServingStatus.SERVING);
        server.awaitTermination();;
    }
}
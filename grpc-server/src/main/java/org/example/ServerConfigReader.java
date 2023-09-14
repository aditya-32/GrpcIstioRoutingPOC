package org.example;

import io.grpc.Server;
import lombok.RequiredArgsConstructor;
import org.aeonbits.owner.ConfigFactory;


public class ServerConfigReader {
    private static final ServerConfig config = ConfigFactory.create(ServerConfig.class);

    public static ServerConfig getServerConfig() {
        return config;
    }
}

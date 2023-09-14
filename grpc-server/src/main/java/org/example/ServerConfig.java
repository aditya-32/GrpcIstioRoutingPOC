package org.example;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:serverapplication.properties"})
public interface ServerConfig extends Config {
    @Key("grpc.port")
    int grpcPort();

    @Key("server.subset")
    String serverSubset();
}

package org.example;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:clientapplication.properties",
        "system:properties",
        "system:env"
})
public interface AppConfig extends Config {
    @Key("grpc-server.target")
    String grpcServerTarget();

    @Key("grpc.load.balance.policy")
    String grpcLoadBalancingPolicy();

    @Key("http.maxThreads")
    String httpMaxThread();

    @Key("http.minSpareThreads")
    String httpMinSpareThread();

    @Key("http.connectionTimeout")
    String httpConnectionTimeout();

    @Key("http.port")
    @DefaultValue("8080")
    int httpPort();
}

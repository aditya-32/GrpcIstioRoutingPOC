package org.example;

import com.example.grpcproto.GrpcIstioRoutingServerGrpc;
import com.example.grpcproto.GrpcRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

public class GrpcClientHandler {
    private static final AppConfig config = AppConfigReader.getAppConfig();
    private static final String GRPC_SERVER_ADDRESS = System.getenv("GRPC_SERVER_ADDRESS");

    private final GrpcIstioRoutingServerGrpc.GrpcIstioRoutingServerBlockingStub grpcRoutingServerStub;

    public GrpcClientHandler(ManagedChannel channel) {
        this.grpcRoutingServerStub = GrpcIstioRoutingServerGrpc.newBlockingStub(channel);
    }

    public static GrpcClientHandler create(){
        var target = GRPC_SERVER_ADDRESS != null ? GRPC_SERVER_ADDRESS : config.grpcServerTarget();
        System.out.println("TARGET : " + target);
        var managedChannel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .directExecutor()
                .defaultLoadBalancingPolicy(config.grpcLoadBalancingPolicy())
                .build();
        GrpcClientHandler.GrpcChannelShutdownHook.register("ServerSubsetChannel", managedChannel);
        return new GrpcClientHandler(managedChannel);
    }

    public String getServerSubset() {
        var requestBuilder = GrpcRequest.newBuilder().setClient("aditya-grpc-client");
        var response = grpcRoutingServerStub.getServerSubset(requestBuilder.build());
        return response.getServerSubset();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GrpcChannelShutdownHook extends Thread {
        private final String name;
        private final ManagedChannel channel;

        public static void register(String name, ManagedChannel channel) {
            Runtime.getRuntime().addShutdownHook(new GrpcChannelShutdownHook(name, channel));
        }

        @Override
        public void run() {
            System.err.printf("ShuttingDown channel %s\n", name);
            try {
                try {
                    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } finally {
                    channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(exception);
            }
            System.err.printf("Channel %s shutDown", name);
        }
    }
}

package org.example;

import com.example.grpcproto.GrpcIstioRoutingServerGrpc;
import com.example.grpcproto.GrpcRequest;
import io.grpc.*;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

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

    public String getServerSubset(String server) {
        var requestBuilder = GrpcRequest.newBuilder().setClient("aditya-grpc-client");
        var interceptor = new GrpcInterceptor(server);
        var response = grpcRoutingServerStub.withInterceptors(interceptor).getServerSubset(requestBuilder.build());
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

    public static class GrpcInterceptor implements ClientInterceptor {
        private final String serverName;

        public GrpcInterceptor(String serverName) {
            this.serverName = serverName;
        }

        @Override
        public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
            return new ForwardingClientCall.SimpleForwardingClientCall<>(channel.newCall(methodDescriptor, callOptions)){
                @Override
                public void start(Listener<RespT> responseListener, Metadata headers) {
                    headers.put(Metadata.Key.of("SERVER", ASCII_STRING_MARSHALLER), serverName);
                    super.start(responseListener, headers);
                }
            };
        }
    }
}

package org.example;

import com.example.grpcproto.GrpcIstioRoutingServerGrpc;
import com.example.grpcproto.GrpcRequest;
import com.example.grpcproto.GrpcResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GrpcServerCustomImpl extends GrpcIstioRoutingServerGrpc.GrpcIstioRoutingServerImplBase {
    private static final String DEFAULT_SERVER_SUBSET =
            ServerConfigReader.getServerConfig().serverSubset();
    private static final String SERVER = System.getenv("SERVER");

    @Override
    public void getServerSubset(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) {
        System.out.printf("Received Request from Client %s", request.getClient());
        var response = GrpcResponse.newBuilder()
                .setServerSubset(SERVER != null ? SERVER : DEFAULT_SERVER_SUBSET)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

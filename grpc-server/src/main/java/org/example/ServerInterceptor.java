package org.example;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;

public class ServerInterceptor implements io.grpc.ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
            Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String serverHeaderValue = metadata.get(Metadata.Key.of("SERVER", Metadata.ASCII_STRING_MARSHALLER));
        System.out.println("SERVER Value in call :" + serverHeaderValue );
        return serverCallHandler.startCall(serverCall, metadata);
    }
}

package org.example;

import com.google.inject.Provider;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class GrpcClientHandlerProvider implements Provider<GrpcClientHandler> {
    @Override
    public GrpcClientHandler get() {
        return GrpcClientHandler.create();
    }
}

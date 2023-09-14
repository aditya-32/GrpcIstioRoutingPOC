package org.example;

import com.google.gson.Gson;
import com.google.inject.PrivateModule;
import static com.google.inject.Scopes.SINGLETON;

public class ApplicationModule extends PrivateModule {
    @Override
    protected void configure() {
        binder().disableCircularProxies();
        binder().requireExplicitBindings();
        binder().requireExactBindingAnnotations();
        binder().requireAtInjectOnConstructors();

        requestStaticInjection(ServerSubsetTestServlet.class);
        bind(GrpcClientHandlerProvider.class).in(SINGLETON);
        bind(GsonProvider.class).in(SINGLETON);
        bind(Gson.class).toProvider(GsonProvider.class).in(SINGLETON);
        bind(GrpcClientHandler.class).toProvider(GrpcClientHandlerProvider.class).in(SINGLETON);
    }
}

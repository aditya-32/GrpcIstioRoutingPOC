package org.example;

import com.google.inject.Guice;
import com.google.inject.Stage;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var injector = Guice.createInjector(Stage.PRODUCTION, new ApplicationModule());
        var applicationInitializer = injector.getInstance(ApplicationInitializer.class);
        applicationInitializer.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}

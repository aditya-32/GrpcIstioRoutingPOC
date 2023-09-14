package org.example;

import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.aeonbits.owner.ConfigFactory;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class AppConfigReader {
    private static final AppConfig config = ConfigFactory.create(AppConfig.class);

    public static AppConfig getAppConfig() {
        return config;
    }
}

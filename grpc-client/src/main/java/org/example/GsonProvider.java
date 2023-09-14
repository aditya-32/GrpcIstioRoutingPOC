package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provider;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;


@NoArgsConstructor(onConstructor = @__(@Inject))
public class GsonProvider implements Provider<Gson> {
    @Override
    public Gson get() {
        return new GsonBuilder().create();
    }
}

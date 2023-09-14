package org.example;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ApplicationInitializer {

    public void init() {
        System.out.println("-------Application Initialised-------");
    }
}

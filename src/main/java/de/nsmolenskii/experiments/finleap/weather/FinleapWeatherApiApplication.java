package de.nsmolenskii.experiments.finleap.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinleapWeatherApiApplication {

    public static final String ROOT_PACKAGE = FinleapWeatherApiApplication.class.getPackage().getName();

    public static void main(String[] args) {
        SpringApplication.run(FinleapWeatherApiApplication.class, args);
    }
}

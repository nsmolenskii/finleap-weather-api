package de.nsmolenskii.experiments.finleap.weather.config;

import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeShapeConfig {

    @Bean
    public TimeZoneEngine timeZoneEngine() {
        return TimeZoneEngine.initialize();
    }

}

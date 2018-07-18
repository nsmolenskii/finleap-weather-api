package de.nsmolenskii.experiments.finleap.weather.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.TreeMap;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(OpenWeatherConfig.Properties.class)
public class OpenWeatherConfig {

    private final Properties properties;

    @Bean
    @Qualifier("owm")
    public WebClient owmWebClient(final WebClient.Builder builder) {
        return builder.baseUrl(properties.getApiUrl())
                .defaultUriVariables(properties.getParams())
                .build();
    }

    @Data
    @Validated
    @ConfigurationProperties(prefix = "gateways.open-weather")
    public static class Properties {

        @NotEmpty
        private String apiUrl;

        @NotEmpty
        private Map<String, Object> params = new TreeMap<>();

    }

}

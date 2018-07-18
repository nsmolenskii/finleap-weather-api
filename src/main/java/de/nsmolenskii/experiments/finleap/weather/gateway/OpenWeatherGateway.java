package de.nsmolenskii.experiments.finleap.weather.gateway;

import de.nsmolenskii.experiments.finleap.weather.config.OpenWeatherConfig;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.problem.CityNotFoundProblem;
import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Gateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Gateway
@Import(OpenWeatherConfig.class)
public class OpenWeatherGateway {

    private final WebClient webClient;

    public OpenWeatherGateway(@Qualifier("owm") final WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<OwmCityForecastDTO> getCityForecast(final String city, @Nullable final String country) {
        final String query = Stream.of(city, country).filter(Objects::nonNull).collect(Collectors.joining(","));
        return webClient.get().uri("/forecast?q={q}", Collections.singletonMap("q", query))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .onStatus(NOT_FOUND::equals, $ -> Mono.error(new CityNotFoundProblem(city, country)))
                .bodyToMono(OwmCityForecastDTO.class);
    }


}

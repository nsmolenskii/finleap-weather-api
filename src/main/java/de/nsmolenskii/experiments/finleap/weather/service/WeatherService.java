package de.nsmolenskii.experiments.finleap.weather.service;

import de.nsmolenskii.experiments.finleap.weather.dto.CityForecastDTO;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

public interface WeatherService {

    Mono<CityForecastDTO> getCityForecast(String city, @Nullable String country);

}

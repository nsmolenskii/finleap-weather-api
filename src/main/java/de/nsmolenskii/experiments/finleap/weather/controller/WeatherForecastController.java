package de.nsmolenskii.experiments.finleap.weather.controller;

import de.nsmolenskii.experiments.finleap.weather.dto.CityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.service.WeatherService;
import de.nsmolenskii.experiments.finleap.weather.utils.annotation.CountyCode;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;

@Validated
@RestController
@AllArgsConstructor
public class WeatherForecastController {

    private final WeatherService service;

    @GetMapping(path = "/api/weather-forecast", produces = "application/json")
    public Mono<CityForecastDTO> getCityWeather(
            @RequestParam(required = false) @NotEmpty final String city,
            @RequestParam(required = false) @CountyCode final String country
    ) {
        return service.getCityForecast(city, country);
    }

}

package de.nsmolenskii.experiments.finleap.weather.service.impl;

import de.nsmolenskii.experiments.finleap.weather.dto.CityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.OpenWeatherGateway;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCoordinatesDTO;
import de.nsmolenskii.experiments.finleap.weather.mappers.OpenWeatherMapper;
import de.nsmolenskii.experiments.finleap.weather.service.TimeZoneService;
import de.nsmolenskii.experiments.finleap.weather.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class OpenWeatherService implements WeatherService {

    private final OpenWeatherGateway gateway;
    private final TimeZoneService timeZoneService;
    private final OpenWeatherMapper mapper;

    @Override
    public Mono<CityForecastDTO> getCityForecast(final String city, @Nullable final String country) {
        return gateway.getCityForecast(city, country).zipWhen(this::getTimeZone, mapper::mapCityForecast);
    }

    private Mono<ZoneId> getTimeZone(final OwmCityForecastDTO forecast) {
        final OwmCoordinatesDTO coordinates = forecast.getCity().getCoordinates();
        return timeZoneService.resolve(coordinates.getLatitude(), coordinates.getLongitude());
    }


}

package de.nsmolenskii.experiments.finleap.weather.service.impl;

import de.nsmolenskii.experiments.finleap.weather.dto.CityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.OpenWeatherGateway;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCoordinatesDTO;
import de.nsmolenskii.experiments.finleap.weather.mappers.OpenWeatherMapper;
import de.nsmolenskii.experiments.finleap.weather.service.TimeZoneService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@RunWith(MockitoJUnitRunner.class)
public class OpenWeatherServiceTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);

    private final double latitude = new Random().nextDouble();
    private final double longitude = new Random().nextDouble();
    private final String message = UUID.randomUUID().toString();

    @Mock
    private OpenWeatherGateway gateway;
    @Mock
    private TimeZoneService timeZoneService;
    @Mock
    private OpenWeatherMapper mapper;

    @InjectMocks
    private OpenWeatherService service;

    @Mock
    private OwmCityForecastDTO owmCityForecastDTO;
    @Mock
    private OwmCityDTO owmCityDTO;
    @Mock
    private OwmCoordinatesDTO coordinatesDTO;
    @Mock
    private CityForecastDTO cityForecastDTO;
    @Mock
    private ZoneId zoneId;

    @Test
    public void shouldProcessValidRequest() {
        doReturn(Mono.just(owmCityForecastDTO)).when(gateway).getCityForecast(anyString(), anyString());
        doReturn(owmCityDTO).when(owmCityForecastDTO).getCity();
        doReturn(coordinatesDTO).when(owmCityDTO).getCoordinates();
        doReturn(latitude).when(coordinatesDTO).getLatitude();
        doReturn(longitude).when(coordinatesDTO).getLongitude();
        doReturn(Mono.just(zoneId)).when(timeZoneService).resolve(anyDouble(), anyDouble());
        doReturn(cityForecastDTO).when(mapper).mapCityForecast(any(), any());

        final CityForecastDTO actual = service.getCityForecast("Berlin", "DE").block();

        assertThat(actual).isEqualTo(cityForecastDTO);
        verify(timeZoneService).resolve(latitude, longitude);
        verify(mapper).mapCityForecast(owmCityForecastDTO, zoneId);
    }

    @Test
    public void shouldRethrowProblemsOnGateway() {
        final IllegalArgumentException error = new IllegalArgumentException(message);
        doReturn(Mono.error(error)).when(gateway).getCityForecast(anyString(), anyString());

        assertThatThrownBy(() -> service.getCityForecast("Berlin", "DE").block()).isSameAs(error);

        verifyNoMoreInteractions(mapper, timeZoneService);
    }

    @Test
    public void shouldRethrowProblemsOnTimezone() {
        final IllegalArgumentException error = new IllegalArgumentException(message);
        doReturn(Mono.just(owmCityForecastDTO)).when(gateway).getCityForecast(anyString(), anyString());
        doReturn(owmCityDTO).when(owmCityForecastDTO).getCity();
        doReturn(coordinatesDTO).when(owmCityDTO).getCoordinates();
        doReturn(latitude).when(coordinatesDTO).getLatitude();
        doReturn(longitude).when(coordinatesDTO).getLongitude();
        doReturn(Mono.error(error)).when(timeZoneService).resolve(anyDouble(), anyDouble());

        assertThatThrownBy(() -> service.getCityForecast("Berlin", "DE").block()).isSameAs(error);

        verify(timeZoneService).resolve(latitude, longitude);
        verifyNoMoreInteractions(mapper, timeZoneService);
    }

    @Test
    public void shouldRethrowProblemsOnMapping() {
        final IllegalArgumentException error = new IllegalArgumentException(message);
        doReturn(Mono.just(owmCityForecastDTO)).when(gateway).getCityForecast(anyString(), anyString());
        doReturn(owmCityDTO).when(owmCityForecastDTO).getCity();
        doReturn(coordinatesDTO).when(owmCityDTO).getCoordinates();
        doReturn(latitude).when(coordinatesDTO).getLatitude();
        doReturn(longitude).when(coordinatesDTO).getLongitude();
        doReturn(Mono.just(zoneId)).when(timeZoneService).resolve(anyDouble(), anyDouble());
        doThrow(error).when(mapper).mapCityForecast(any(), any());

        assertThatThrownBy(() -> service.getCityForecast("Berlin", "DE").block()).isSameAs(error);

        verify(timeZoneService).resolve(latitude, longitude);
        verify(mapper).mapCityForecast(owmCityForecastDTO, zoneId);
        verifyNoMoreInteractions(mapper, timeZoneService);
    }
}
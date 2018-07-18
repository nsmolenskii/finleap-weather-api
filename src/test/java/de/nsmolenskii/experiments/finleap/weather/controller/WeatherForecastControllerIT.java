package de.nsmolenskii.experiments.finleap.weather.controller;


import de.nsmolenskii.experiments.finleap.weather.dto.CityDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.CityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.DayForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.DayPart;
import de.nsmolenskii.experiments.finleap.weather.service.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = WeatherForecastController.class)
public class WeatherForecastControllerIT {

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private MockMvc mockMvc;
    private final CityForecastDTO forecast = CityForecastDTO.builder()
            .city(CityDTO.of("Berlin", "DE"))
            .addForecasts(DayForecastDTO.builder()
                    .day(LocalDate.parse("2018-07-17"))
                    .pressure(12.34)
                    .putTemperature(DayPart.DAY, 23.45)
                    .putTemperature(DayPart.NIGHT, 34.56)
                    .build())
            .build();


    @Test
    public void shouldThrowWhenNoCityProvided() throws Exception {
        mockMvc.perform(get("/api/weather-forecast"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("title", containsString("Constraint Violation")))
                .andExpect(jsonPath("type", containsString("/problem/constraint-violation")))
                .andExpect(jsonPath("violations", hasSize(1)))
                .andExpect(jsonPath("violations[0]field", containsString("city")))
                .andExpect(jsonPath("violations[0]message", containsString("must not be empty")));

        verifyNoMoreInteractions(weatherService);
    }

    @Test
    public void shouldThrowWhenInvalidCountryProvided() throws Exception {
        mockMvc.perform(get("/api/weather-forecast")
                .param("city", "Berlin")
                .param("country", "42"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("title", containsString("Constraint Violation")))
                .andExpect(jsonPath("type", containsString("/problem/constraint-violation")))
                .andExpect(jsonPath("violations", hasSize(1)))
                .andExpect(jsonPath("violations[0]field", containsString("country")))
                .andExpect(jsonPath("violations[0]message", containsString("Invalid country code: 42")))
                .andExpect(jsonPath("violations[0]message", containsString("Available values are: AD, AE, AF")));

        verifyNoMoreInteractions(weatherService);
    }

    @Test
    public void shouldProcessValidRequest() throws Exception {
        doReturn(Mono.just(forecast)).when(weatherService).getCityForecast(anyString(), nullable(String.class));

        final MvcResult mvcResult = mockMvc.perform(get("/api/weather-forecast")
                .param("city", "Berlin"))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("city.name", is("Berlin")))
                .andExpect(jsonPath("city.country", is("DE")))
                .andExpect(jsonPath("forecasts", hasSize(1)))
                .andExpect(jsonPath("forecasts[0]day", is("2018-07-17")))
                .andExpect(jsonPath("forecasts[0]pressure", is(12.34)))
                .andExpect(jsonPath("forecasts[0]temperature.day", is(23.45)))
                .andExpect(jsonPath("forecasts[0]temperature.night", is(34.56)))
        ;

        verify(weatherService).getCityForecast("Berlin", null);
    }

    @Test
    public void shouldProcessValidRequestWithCountry() throws Exception {
        doReturn(Mono.just(forecast)).when(weatherService).getCityForecast(anyString(), anyString());

        final MvcResult mvcResult = mockMvc.perform(get("/api/weather-forecast")
                .param("city", "Berlin")
                .param("country", "DE"))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("city.name", is("Berlin")))
                .andExpect(jsonPath("city.country", is("DE")))
                .andExpect(jsonPath("forecasts", hasSize(1)))
                .andExpect(jsonPath("forecasts[0]day", is("2018-07-17")))
                .andExpect(jsonPath("forecasts[0]pressure", is(12.34)))
                .andExpect(jsonPath("forecasts[0]temperature.day", is(23.45)))
                .andExpect(jsonPath("forecasts[0]temperature.night", is(34.56)))
        ;

        verify(weatherService).getCityForecast("Berlin", "DE");
    }
}
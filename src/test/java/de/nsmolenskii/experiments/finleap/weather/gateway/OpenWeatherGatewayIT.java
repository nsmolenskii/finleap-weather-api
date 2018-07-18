package de.nsmolenskii.experiments.finleap.weather.gateway;

import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCoordinatesDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmForecastDataDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmInstantForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.problem.CityNotFoundProblem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("gateway")
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 0)
public class OpenWeatherGatewayIT {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);

    @Autowired
    private OpenWeatherGateway gateway;


    @Test
    public void shouldMapValidResponseFromWireMock() {
        final OwmCityForecastDTO block = gateway.getCityForecast("Berlin", "DE").block();

        assertThat(block).isEqualTo(OwmCityForecastDTO.builder()
                .city(OwmCityDTO.builder()
                        .name("Berlin")
                        .country("DE")
                        .coordinates(OwmCoordinatesDTO.of(52.517, 13.3889))
                        .build())
                .addForecasts(OwmInstantForecastDTO.builder()
                        .forecastFor(Instant.parse("2018-07-17T18:00:00Z"))
                        .data(OwmForecastDataDTO.of(295.13, 1021.22))
                        .build())
                .build());
    }

    @Test
    public void shouldMapNotFoundResponseFromWireMock() {
        assertThatThrownBy(() -> gateway.getCityForecast("Berlin", "RU").block())
                .isEqualToComparingFieldByField(new CityNotFoundProblem("Berlin", "RU"));
    }


}
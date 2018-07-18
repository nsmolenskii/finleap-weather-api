package de.nsmolenskii.experiments.finleap.weather.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmForecastDataDTOBuilder.ImmutableOwmForecastDataDTO;
import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Json;
import org.immutables.value.Value;

@Json
@Value.Immutable
@JsonSerialize(as = ImmutableOwmForecastDataDTO.class)
@JsonDeserialize(as = ImmutableOwmForecastDataDTO.class)
public interface OwmForecastDataDTO {

    @Value.Parameter
    @JsonProperty("temp")
    double getTemperature();

    @Value.Parameter
    double getPressure();

    static OwmForecastDataDTO of(final double temperature, final double pressure) {
        return ImmutableOwmForecastDataDTO.of(temperature, pressure);
    }
}

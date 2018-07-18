package de.nsmolenskii.experiments.finleap.weather.dto;

import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Json;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.Map;

@Json
@Value.Immutable
public interface DayForecastDTO {

    LocalDate getDay();

    double getPressure();

    Map<DayPart, Double> getTemperature();

    static DayForecastDTOBuilder builder() {
        return new DayForecastDTOBuilder();
    }
}

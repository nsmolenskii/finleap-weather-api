package de.nsmolenskii.experiments.finleap.weather.dto;


import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Json;
import org.immutables.value.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Json
@Value.Immutable
public interface CityForecastDTO {

    @Valid
    @NotNull
    CityDTO getCity();

    @Valid
    @NotEmpty
    List<DayForecastDTO> getForecasts();

    static CityForecastDTOBuilder builder() {
        return new CityForecastDTOBuilder();
    }


}

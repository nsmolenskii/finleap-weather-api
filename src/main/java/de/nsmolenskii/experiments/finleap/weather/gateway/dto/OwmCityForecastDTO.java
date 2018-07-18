package de.nsmolenskii.experiments.finleap.weather.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityForecastDTOBuilder.ImmutableOwmCityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Json;
import org.immutables.value.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Json
@Value.Immutable
@JsonSerialize(as = ImmutableOwmCityForecastDTO.class)
@JsonDeserialize(as = ImmutableOwmCityForecastDTO.class)
public interface OwmCityForecastDTO {

    @Valid
    @NotNull
    OwmCityDTO getCity();

    @Valid
    @NotEmpty
    @JsonProperty("list")
    List<OwmInstantForecastDTO> getForecasts();

    static OwmCityForecastDTOBuilder builder() {
        return new OwmCityForecastDTOBuilder();
    }

}






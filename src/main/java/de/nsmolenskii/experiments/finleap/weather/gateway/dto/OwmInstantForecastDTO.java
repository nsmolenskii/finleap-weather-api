package de.nsmolenskii.experiments.finleap.weather.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmInstantForecastDTOBuilder.ImmutableOwmInstantForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Json;
import org.immutables.value.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Json
@Value.Immutable
@JsonSerialize(as = ImmutableOwmInstantForecastDTO.class)
@JsonDeserialize(as = ImmutableOwmInstantForecastDTO.class)
public interface OwmInstantForecastDTO {

    @NotNull
    @JsonProperty("dt")
    Instant getForecastFor();

    @Valid
    @JsonProperty("main")
    OwmForecastDataDTO getData();

    static OwmInstantForecastDTOBuilder builder(){
        return new OwmInstantForecastDTOBuilder();
    }
}

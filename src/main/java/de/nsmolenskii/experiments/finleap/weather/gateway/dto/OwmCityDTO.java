package de.nsmolenskii.experiments.finleap.weather.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityDTOBuilder.ImmutableOwmCityDTO;
import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Json;
import org.immutables.value.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Json
@Value.Immutable
@JsonSerialize(as = ImmutableOwmCityDTO.class)
@JsonDeserialize(as = ImmutableOwmCityDTO.class)
public interface OwmCityDTO {

    @NotEmpty
    String getName();

    @NotEmpty
    String getCountry();

    @Valid
    @NotNull
    @JsonProperty("coord")
    OwmCoordinatesDTO getCoordinates();

    static OwmCityDTOBuilder builder() {
        return new OwmCityDTOBuilder();
    }

}
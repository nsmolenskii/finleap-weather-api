package de.nsmolenskii.experiments.finleap.weather.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCoordinatesDTOBuilder.ImmutableOwmCoordinatesDTO;
import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Json;
import org.immutables.value.Value;

@Json
@Value.Immutable
@JsonSerialize(as = ImmutableOwmCoordinatesDTO.class)
@JsonDeserialize(as = ImmutableOwmCoordinatesDTO.class)
public interface OwmCoordinatesDTO {

    @Value.Parameter
    @JsonProperty("lat")
    double getLatitude();

    @Value.Parameter
    @JsonProperty("lon")
    double getLongitude();

    static OwmCoordinatesDTO of(final double latitude, final double longitude) {
        return ImmutableOwmCoordinatesDTO.of(latitude, longitude);
    }
}
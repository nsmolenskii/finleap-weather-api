package de.nsmolenskii.experiments.finleap.weather.dto;

import de.nsmolenskii.experiments.finleap.weather.utils.annotation.Json;
import org.immutables.value.Value;

import javax.validation.constraints.NotEmpty;

@Json
@Value.Immutable
public interface CityDTO {

    @NotEmpty
    @Value.Parameter
    String getName();

    @NotEmpty
    @Value.Parameter
    String getCountry();

    static CityDTO of(final String city, final String country) {
        return CityDTOBuilder.ImmutableCityDTO.of(city, country);
    }
}

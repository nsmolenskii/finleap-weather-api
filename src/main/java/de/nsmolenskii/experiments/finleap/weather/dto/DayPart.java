package de.nsmolenskii.experiments.finleap.weather.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DayPart {
    DAY, NIGHT;

    @JsonValue
    public String getId() {
        return name().toLowerCase();
    }

}


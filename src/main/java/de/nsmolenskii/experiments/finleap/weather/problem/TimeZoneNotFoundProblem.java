package de.nsmolenskii.experiments.finleap.weather.problem;

import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@Getter
public class TimeZoneNotFoundProblem extends AbstractThrowableProblem {

    private static final String TYPE_NAME = "/problems/unknown-timezone";

    private final double latitude;
    private final double longitude;

    public TimeZoneNotFoundProblem(final double latitude, final double longitude) {
        super(URI.create(TYPE_NAME), "Could not resolve timezone for given coordinates", Status.INTERNAL_SERVER_ERROR);
        this.latitude = latitude;
        this.longitude = longitude;
    }

}

package de.nsmolenskii.experiments.finleap.weather.problem;

import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import javax.annotation.Nullable;
import java.net.URI;

@Getter
public class CityNotFoundProblem extends AbstractThrowableProblem {

    private static final String TYPE_NAME = "/problems/unknown-city";

    private final String city;
    private final String country;

    public CityNotFoundProblem(final String city, @Nullable final String country) {
        super(URI.create(TYPE_NAME), "Unknown city", Status.NOT_FOUND);
        this.city = city;
        this.country = country;
    }

}

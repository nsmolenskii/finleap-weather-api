package de.nsmolenskii.experiments.finleap.weather.service.impl;

import de.nsmolenskii.experiments.finleap.weather.problem.TimeZoneNotFoundProblem;
import de.nsmolenskii.experiments.finleap.weather.service.TimeZoneService;
import lombok.AllArgsConstructor;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZoneId;

@Service
@AllArgsConstructor
public class TimeShapeTimeZoneService implements TimeZoneService {

    private final TimeZoneEngine engine;

    @Override
    public Mono<ZoneId> resolve(final double latitude, final double longitude) {
        return engine.query(latitude, longitude).map(Mono::just)
                .orElseGet(() -> Mono.error(new TimeZoneNotFoundProblem(latitude, longitude)));
    }
}

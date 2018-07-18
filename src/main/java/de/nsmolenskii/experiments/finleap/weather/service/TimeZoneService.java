package de.nsmolenskii.experiments.finleap.weather.service;

import reactor.core.publisher.Mono;

import java.time.ZoneId;

public interface TimeZoneService {

    Mono<ZoneId> resolve(double latitude, double longitude);

}

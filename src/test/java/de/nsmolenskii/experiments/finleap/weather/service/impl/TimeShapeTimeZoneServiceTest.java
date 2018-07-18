package de.nsmolenskii.experiments.finleap.weather.service.impl;

import de.nsmolenskii.experiments.finleap.weather.problem.TimeZoneNotFoundProblem;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZoneId;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class TimeShapeTimeZoneServiceTest {
    @Rule
    public Timeout globalTimeout= Timeout.seconds(1);

    @Mock
    private TimeZoneEngine engine;

    @InjectMocks
    private TimeShapeTimeZoneService service;

    @Mock
    private ZoneId zoneId;

    private final double latitude = new Random().nextDouble();
    private final double longitude = new Random().nextDouble();

    @Test
    public void shouldResolveValidCoordinates() {
        doReturn(Optional.of(zoneId)).when(engine).query(anyDouble(), anyDouble());

        final ZoneId actual = service.resolve(latitude, longitude).block();

        assertThat(actual).isSameAs(zoneId);
        verify(engine).query(latitude, longitude);
    }

    @Test
    public void shouldThrowOnInvalidCoordinates() {
        doReturn(Optional.empty()).when(engine).query(anyDouble(), anyDouble());

        assertThatThrownBy(() -> service.resolve(latitude, longitude).block())
                .isInstanceOf(TimeZoneNotFoundProblem.class);
        verify(engine).query(latitude, longitude);
    }
}
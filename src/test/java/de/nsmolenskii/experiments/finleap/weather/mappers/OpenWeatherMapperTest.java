package de.nsmolenskii.experiments.finleap.weather.mappers;


import com.google.common.collect.ImmutableList;
import de.nsmolenskii.experiments.finleap.weather.dto.CityDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.CityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.DayForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.DayPart;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmForecastDataDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmInstantForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.mappers.OpenWeatherMapper.DayForecastHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenWeatherMapperTest {


    @Spy
    @InjectMocks
    private OpenWeatherMapper mapper;


    @Mock
    private OwmCityForecastDTO owmCityForecastDTO;
    @Mock
    private OwmCityDTO owmCityDTO;
    @Mock
    private OwmInstantForecastDTO owmInstantForecastDTO;
    @Mock
    private OwmForecastDataDTO owmForecastDataDTO;

    @Mock
    private ZoneId zoneId;
    @Mock
    private CityDTO cityDTO;
    @Mock
    private DayForecastDTO dayForecastDTO;
    @Mock
    private DayForecastHelper helper1;
    @Mock
    private DayForecastHelper helper2;
    @Mock
    private DayForecastHelper helper3;

    @Test
    public void shouldMapCityForecast() {
        doReturn(owmCityDTO).when(owmCityForecastDTO).getCity();
        doReturn(ImmutableList.of(owmInstantForecastDTO)).when(owmCityForecastDTO).getForecasts();
        doReturn(cityDTO).when(mapper).mapCity(any());
        doReturn(ImmutableList.of(dayForecastDTO)).when(mapper).mapForecasts(anyList(), any(ZoneId.class));

        final CityForecastDTO actual = mapper.mapCityForecast(owmCityForecastDTO, zoneId);

        assertThat(actual).isEqualTo(CityForecastDTO.builder()
                .city(cityDTO)
                .addForecasts(dayForecastDTO)
                .build());

        verify(mapper).mapCity(owmCityDTO);
        verify(mapper).mapForecasts(ImmutableList.of(owmInstantForecastDTO), zoneId);
    }

    @Test
    public void shouldMapCityDto() {
        final CityDTO actual = mapper.mapCity(OwmCityDTO.builder()
                .name("Berlin")
                .country("DE")
                .build());

        assertThat(actual).isEqualTo(CityDTO.of("Berlin", "DE"));
    }

    @Test
    public void shouldMapForecast() {
        final ImmutableList<DayForecastHelper> helpers = ImmutableList.of(helper1, helper2);
        doReturn(helpers).when(mapper).classify(any(ZoneId.class), any(OwmInstantForecastDTO.class));
        doReturn(LocalDate.now().plusDays(2)).when(helper1).getDay();
        doReturn(LocalDate.now().plusDays(2)).when(helper2).getDay();
        doReturn(helper3).when(helper1).merge(helper2);
        doReturn(dayForecastDTO).when(helper3).toDTO();

        final List<DayForecastDTO> actual = mapper.mapForecasts(ImmutableList.of(owmInstantForecastDTO), zoneId);

        assertThat(actual).containsExactly(dayForecastDTO);
        verify(mapper).classify(zoneId, owmInstantForecastDTO);
    }

    @Test
    public void shouldClassifyInstantForecastRelatedToDay() {
        doReturn(LocalDate.now().atTime(12, 34).toInstant(ZoneOffset.UTC)).when(owmInstantForecastDTO).getForecastFor();
        doReturn(owmForecastDataDTO).when(owmInstantForecastDTO).getData();

        final Collection<DayForecastHelper> actual = mapper.classify(ZoneId.of("Europe/Berlin"), owmInstantForecastDTO);

        assertThat(actual).containsExactly(DayForecastHelper.of(LocalDate.now(), DayPart.DAY, owmForecastDataDTO));
    }

    @Test
    public void shouldClassifyInstantForecastRelatedToNight() {
        doReturn(LocalDate.now().atTime(23, 45).toInstant(ZoneOffset.UTC)).when(owmInstantForecastDTO).getForecastFor();
        doReturn(owmForecastDataDTO).when(owmInstantForecastDTO).getData();

        final Collection<DayForecastHelper> actual = mapper.classify(ZoneId.of("Europe/Berlin"), owmInstantForecastDTO);

        assertThat(actual).containsExactly(DayForecastHelper.of(LocalDate.now(), DayPart.NIGHT, owmForecastDataDTO));
    }

    @Test
    public void shouldClassifyInstantForecastRelatedToPreviousNight() {
        doReturn(LocalDate.now().atTime(1, 23).toInstant(ZoneOffset.UTC)).when(owmInstantForecastDTO).getForecastFor();
        doReturn(owmForecastDataDTO).when(owmInstantForecastDTO).getData();

        final Collection<DayForecastHelper> actual = mapper.classify(ZoneId.of("Europe/Berlin"), owmInstantForecastDTO);

        final LocalDate yesterday = LocalDate.now().minusDays(1);
        assertThat(actual).containsExactly(DayForecastHelper.of(yesterday, DayPart.NIGHT, owmForecastDataDTO));
    }
}
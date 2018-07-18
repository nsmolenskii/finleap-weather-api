package de.nsmolenskii.experiments.finleap.weather.mappers;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import de.nsmolenskii.experiments.finleap.weather.dto.CityDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.CityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.DayForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.dto.DayPart;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmCityForecastDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmForecastDataDTO;
import de.nsmolenskii.experiments.finleap.weather.gateway.dto.OwmInstantForecastDTO;
import org.immutables.value.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Component
public class OpenWeatherMapper {

    public CityForecastDTO mapCityForecast(OwmCityForecastDTO forecast, ZoneId zoneId) {
        return CityForecastDTO.builder()
                .city(mapCity(forecast.getCity()))
                .forecasts(mapForecasts(forecast.getForecasts(), zoneId))
                .build();
    }

    @VisibleForTesting
    protected CityDTO mapCity(final OwmCityDTO city) {
        return CityDTO.of(city.getName(), city.getCountry());
    }

    @VisibleForTesting
    protected List<DayForecastDTO> mapForecasts(final List<OwmInstantForecastDTO> forecasts, final ZoneId zoneId) {
        final Map<LocalDate, List<DayForecastHelper>> forecastsByDate = forecasts.stream()
                .flatMap(forecast -> classify(zoneId, forecast).stream())
                .filter(dayWithinNextThreeDays())
                .collect(Collectors.groupingBy(DayForecastHelper::getDay));

        return forecastsByDate.values().stream()
                .map(helpers -> helpers.stream().reduce(DayForecastHelper::merge).orElse(null))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(DayForecastHelper::getDay))
                .map(DayForecastHelper::toDTO)
                .collect(toList());
    }

    @VisibleForTesting
    protected Collection<DayForecastHelper> classify(final ZoneId zoneId, final OwmInstantForecastDTO forecast) {
        final LocalTime timeBegin = LocalTime.of(6, 0); //TODO configurable
        final LocalTime timeEnd = LocalTime.of(18, 0); //TODO configurable

        final ZonedDateTime dateTime = forecast.getForecastFor().atZone(zoneId);
        final LocalTime time = dateTime.toLocalTime();
        final LocalDate date = dateTime.toLocalDate();

        final ArrayList<DayForecastHelper> classifiedForecasts = new ArrayList<>();

        if (time.compareTo(timeBegin) >= 0 && time.compareTo(timeEnd) <= 0) {
            classifiedForecasts.add(DayForecastHelper.of(date, DayPart.DAY, forecast.getData()));
        }

        if (time.compareTo(timeEnd) >= 0) {
            classifiedForecasts.add(DayForecastHelper.of(date, DayPart.NIGHT, forecast.getData()));
        }

        if (time.compareTo(LocalTime.MIDNIGHT) >= 0 && time.compareTo(timeBegin) <= 0) {
            classifiedForecasts.add(DayForecastHelper.of(date.minusDays(1), DayPart.NIGHT, forecast.getData()));
        }

        return classifiedForecasts;
    }

    private Predicate<DayForecastHelper> dayWithinNextThreeDays() {
        final LocalDate dateBegin = LocalDate.now().plusDays(1);
        final LocalDate dateEnd = LocalDate.now().plusDays(3);

        return forecast -> {
            final LocalDate day = forecast.getDay();
            return day.compareTo(dateBegin) >= 0 && day.compareTo(dateEnd) <= 0;
        };
    }


    @Value.Immutable
    @Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
    interface DayForecastHelper {

        LocalDate getDay();

        Multimap<DayPart, Double> getTemperatures();

        List<Double> getPressure();

        default DayForecastHelper merge(DayForecastHelper that) {
            Preconditions.checkArgument(Objects.equals(this.getDay(), that.getDay()));
            return new DayForecastHelperBuilder().from(this).from(that).build();
        }

        default DayForecastDTO toDTO() {
            final Function<Collection<Double>, Double> avg = values -> values.stream()
                    .mapToDouble(i -> i)
                    .average()
                    .orElse(Double.NaN);

            return DayForecastDTO.builder()
                    .day(getDay())
                    .pressure(avg.apply(getPressure()))
                    .putTemperature(DayPart.DAY, avg.apply(getTemperatures().get(DayPart.DAY)))
                    .putTemperature(DayPart.NIGHT, avg.apply(getTemperatures().get(DayPart.NIGHT)))
                    .build();
        }

        static DayForecastHelper of(LocalDate day, DayPart part, OwmForecastDataDTO data) {
            return new DayForecastHelperBuilder()
                    .day(day)
                    .putTemperatures(part, data.getTemperature())
                    .addPressure(data.getPressure())
                    .build();
        }
    }
}

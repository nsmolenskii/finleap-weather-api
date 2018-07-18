package de.nsmolenskii.experiments.finleap.weather.config;

import de.nsmolenskii.experiments.finleap.weather.utils.logbook.LogbookRequestFilter;
import de.nsmolenskii.experiments.finleap.weather.utils.logbook.LogbookResponseFilter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Conditions;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.RawHttpRequest;
import org.zalando.logbook.spring.LogbookAutoConfiguration;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Configuration
@ImportAutoConfiguration(LogbookAutoConfiguration.class)
public class LoggingConfiguration {

    @Bean
    public Predicate<RawHttpRequest> requestCondition() {
        return Stream.of("/api/**", "/data/**")
                .map(Conditions::<RawHttpRequest>requestTo)
                .reduce(Predicate::or)
                .orElse($ -> true);
    }

    @Bean
    public WebClientCustomizer webClientCustomizer(final Logbook logbook) {
        return builder -> builder
                .filter(new LogbookRequestFilter(logbook))
                .filter(new LogbookResponseFilter());
    }

}

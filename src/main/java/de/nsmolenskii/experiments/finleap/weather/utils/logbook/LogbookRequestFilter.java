package de.nsmolenskii.experiments.finleap.weather.utils.logbook;

import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.zalando.logbook.Correlator;
import org.zalando.logbook.Logbook;
import reactor.core.publisher.Mono;

import java.io.IOException;

@AllArgsConstructor
public class LogbookRequestFilter implements ExchangeFilterFunction {

    private final Logbook logbook;
    private static final Correlator NOOP = response -> {
    };

    @Override
    public Mono<ClientResponse> filter(final ClientRequest request, final ExchangeFunction next) {
        final Correlator correlator = write(request);
        return Mono.just(ClientRequest.from(request).attribute("correlator", correlator).build())
                .flatMap(next::exchange);
    }

    private Correlator write(final ClientRequest request) {
        try {
            return logbook.write(new LocalRequest(request)).orElse(NOOP);
        } catch (IOException e) {
            return NOOP;
        }
    }
}

package de.nsmolenskii.experiments.finleap.weather.utils.logbook;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.zalando.logbook.Correlator;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class LogbookResponseFilter implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(final ClientRequest request, final ExchangeFunction next) {
        return next.exchange(request).flatMap(response -> {
            write(request, response);
            return Mono.just(response);
        });
    }

    private void write(final ClientRequest request, final ClientResponse response) {
        request.attribute("correlator")
                .map(Correlator.class::cast)
                .ifPresent(correlator -> {
                    try {
                        correlator.write(new RemoteResponse(response));
                    } catch (IOException ignore) {
                    }
                });
    }
}

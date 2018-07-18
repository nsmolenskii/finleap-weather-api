package de.nsmolenskii.experiments.finleap.weather.utils.logbook;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.zalando.logbook.HttpResponse;
import org.zalando.logbook.Origin;
import org.zalando.logbook.RawHttpResponse;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class RemoteResponse implements RawHttpResponse, HttpResponse {

    private final ClientResponse response;

    @Override
    public int getStatus() {
        return response.statusCode().value();
    }

    @Override
    public String getProtocolVersion() {
        return "HTTP/1.1";
    }

    @Override
    public Origin getOrigin() {
        return Origin.REMOTE;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return response.headers().asHttpHeaders();
    }

    @Nullable
    @Override
    public String getContentType() {
        return response.headers().contentType()
                .map(MediaType::toString).orElse("");
    }

    @Override
    public Charset getCharset() {
        return response.headers().contentType()
                .map(MediaType::getCharset)
                .orElse(StandardCharsets.UTF_8);
    }

    @Override
    public HttpResponse withBody() throws IOException {
        return this;
    }

    @Override
    public byte[] getBody() throws IOException {
        return new byte[0]; //TODO
    }
}

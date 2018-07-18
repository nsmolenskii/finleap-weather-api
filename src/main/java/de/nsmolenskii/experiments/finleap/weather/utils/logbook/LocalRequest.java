package de.nsmolenskii.experiments.finleap.weather.utils.logbook;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.Origin;
import org.zalando.logbook.RawHttpRequest;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class LocalRequest implements RawHttpRequest, HttpRequest {

    private final ClientRequest request;

    @Override
    public String getProtocolVersion() {
        return "HTTP/1.1";
    }

    @Override
    public Origin getOrigin() {
        return Origin.LOCAL;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return request.headers();
    }

    @Nullable
    @Override
    public String getContentType() {
        return Optional.ofNullable(request.headers().getContentType())
                .map(MediaType::toString)
                .orElse("");
    }

    @Override
    public Charset getCharset() {
        return Optional.ofNullable(request.headers().getContentType())
                .map(MediaType::getCharset)
                .orElse(StandardCharsets.UTF_8);
    }

    @Override
    public String getRemote() {
        return "localhost";
    }

    @Override
    public String getMethod() {
        return request.method().name();
    }

    @Override
    public String getScheme() {
        return request.url().getScheme();
    }

    @Override
    public String getHost() {
        return request.url().getHost();
    }

    @Override
    public Optional<Integer> getPort() {
        return Optional.empty();
    }

    @Override
    public String getPath() {
        return request.url().getPath();
    }

    @Override
    public String getQuery() {
        return request.url().getQuery();
    }

    @Override
    public HttpRequest withBody() throws IOException {
        return this;
    }

    @Override
    public byte[] getBody() throws IOException {
        return new byte[0]; //TODO
    }
}

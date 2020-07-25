package robhopkins.wc.iam.rest;

import org.takes.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

final class RequestBuilder implements Request {

    static RequestBuilder newBuilder() {
        return new RequestBuilder();
    }

    private final Map<String, String> headers = new LinkedHashMap<>();
    private String body;

    private RequestBuilder() {
    }

    RequestBuilder withHeader(final String header, final String value) {
        headers.put(header, value);
        return this;
    }

    RequestBuilder withBody(final String body) {
        this.body = body;
        return this;
    }

    Request build() {
        return this;
    }

    @Override
    public InputStream body() throws IOException {
        return new ByteArrayInputStream(
            Optional.ofNullable(body)
                .map(String::getBytes)
                .orElse(new byte[0])
        );
    }

    @Override
    public Iterable<String> head() throws IOException {
        return headers.entrySet().stream()
            .map(entry -> String.format("%s:%s", entry.getKey(), entry.getValue()))
            .collect(Collectors.toSet());
    }
}

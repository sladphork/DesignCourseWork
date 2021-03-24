package robhopkins.wc.common.datasource;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

final class HttpDatasourceRequest implements DatasourceRequest {

    private final JSONObject payload;

    HttpDatasourceRequest(final JSONObject payload) {
        this.payload = payload;
    }

    @Override
    public JSONObject payload() {
        return payload;
    }

    @Override
    public String type() {
        return payload().getString("type");
    }

    @Override
    public DatasourceResponse execute(final String baseUri) throws IOException {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder(URI.create(String.format("%s/data", baseUri)))
            .POST(HttpRequest.BodyPublishers.ofString(payload.toString(2)))
            .header("Content-type", "application/json")
            .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new HttpDatasourceResponse(response);
        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }
    }

    private static final class HttpDatasourceResponse implements DatasourceResponse {

        private final HttpResponse<String> response;
        HttpDatasourceResponse(final HttpResponse<String> response) {
            this.response = response;
        }

        @Override
        public int status() {
            return response.statusCode();
        }

        @Override
        public String body() {
            return response.body();
        }
    }
}

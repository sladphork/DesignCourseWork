package robhopkins.wc.iam.rest;

import org.json.JSONObject;
import org.takes.Response;
import robhopkins.wc.iam.signin.Token;

import java.util.Base64;

final class SigninResponseBuilder {
    static SigninResponseBuilder newBuilder() {
        return new SigninResponseBuilder("application/vnd.wc.v1+json");
    }

    private final String contentType;
    private Token token;

    private SigninResponseBuilder(final String contentType) {
        this.contentType = contentType;
    }

    SigninResponseBuilder withToken(final Token token) {
        this.token = token;
        return this;
    }

    Response build() {
        return new RestResponse(
            status(),
            contentType, json()
        );
    }

    private String json() {
        return new JSONObject()
            .put("user", token.userId().toString())
            .put("token", tokenAsString())
            .toString(2);
    }

    private String tokenAsString() {
        return Base64.getEncoder().encodeToString(token.toJson().getBytes());
    }

    private int status() {
        return 200;
    }
}

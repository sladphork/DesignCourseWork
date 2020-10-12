package robhopkins.wc.iam.request.signin;

import org.json.JSONObject;
import robhopkins.wc.iam.sheet.Token;

import javax.ws.rs.core.Response;
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
        return Response.status(status())
            .entity(json())
            .header("Content-type", contentType)
            .build();
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

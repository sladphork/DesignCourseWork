package robhopkins.wc.professors.iam;

import io.vertx.core.json.JsonObject;
import robhopkins.wc.professors.auth.AuthenticationException;

import java.time.Instant;

// TODO: Create a factory, etc.
public final class InMemIAM implements IAM {
    @Override
    public Token testToken(final Token token) throws AuthenticationException {

        if (token.toJson().isEmpty() || expired(token)) {
            throw new AuthenticationException();
        }
        return token;
    }

    private boolean expired(final Token token) {
        final JsonObject tokenAsJson = token.toJson();
        final Instant expiration = tokenAsJson.getInstant("exp");
        return expiration.isBefore(Instant.now());
    }

}

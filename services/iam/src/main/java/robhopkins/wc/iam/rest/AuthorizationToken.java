package robhopkins.wc.iam.rest;

import org.json.JSONObject;
import org.takes.Request;
import robhopkins.wc.iam.rest.exception.BadRequest;
import robhopkins.wc.iam.signin.Token;
import robhopkins.wc.iam.user.domain.UserId;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

final class AuthorizationToken implements Token {

    private static final Pattern AUTH_PATTERN = Pattern.compile("(A|a)uthorization: Bearer (.*)");

    static AuthorizationToken from(final Request request) throws BadRequest, IOException {
        final String token =
            StreamSupport.stream(request.head().spliterator(), false)
                .filter(head -> head.matches(AUTH_PATTERN.toString()))
                .findFirst()
                .map(AuthorizationToken::token)
                .orElseThrow(() -> new BadRequest("Missing Token"));

        if (token.isEmpty()) {
            throw new BadRequest("Missing Token");
        }
        return new AuthorizationToken(token);
    }

    private static String token(final String header) {
        final Matcher matcher = AUTH_PATTERN.matcher(header);
        return matcher.matches()
            ? matcher.group(2)
            : "";

    }

    private final JSONObject json;
    private AuthorizationToken(final String json) {
        this.json = new JSONObject(new String(Base64.getDecoder().decode(json.trim())));
    }

    @Override
    public String toJson() {
        return json.toString(2);
    }

    @Override
    public UserId userId() {
        return UserId.from(json.getString("user"));
    }
}

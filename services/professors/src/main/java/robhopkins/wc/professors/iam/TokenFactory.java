package robhopkins.wc.professors.iam;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TokenFactory {
    private static final Map<Class<?>, Function<Object, TokenFactory>> FACTORIES = Map.of(
        HttpServerRequest.class, value -> new RequestTokenFactory((HttpServerRequest) value),
        String.class, value -> new StringTokenFactory((String) value)
    );

    public static TokenFactory from(final Object value) {
        return FACTORIES.entrySet().stream()
            .filter(entry -> entry.getKey().isInstance(value))
            .findFirst()
            .map(entry -> entry.getValue().apply(value))
            .orElse(EMPTY);
    }

    private TokenFactory() {

    }

    /**
     * Creates a {@link Token}.
     *
     * @return a {@link Token} instance.
     */
    public abstract Token create();

    private static final Token EMPTY_TOKEN = new Token() {
        @Override
        public JsonObject toJson() {
            return new JsonObject();
        }
    };

    private static final TokenFactory EMPTY = new TokenFactory() {
        @Override
        public Token create() {
            return EMPTY_TOKEN;
        }
    };

    private static final class RequestTokenFactory extends TokenFactory {
        private static final Pattern AUTH_HEADER = Pattern.compile("Bearer (.+)");

        private final TokenFactory target;

        RequestTokenFactory(final HttpServerRequest request) {
            this.target = new StringTokenFactory(
                token(request)
            );
        }

        @Override
        public Token create() {
            return target.create();
        }

        private String token(final HttpServerRequest request) {
            final Matcher matcher = AUTH_HEADER.matcher(
                header(request)
            );

            if (matcher.matches()) {
                return new String(
                    Base64.getDecoder().decode(
                        matcher.group(1)
                    )
                );
            }

            return "";
        }

        private String header(final HttpServerRequest request) {
            return Optional.ofNullable(request.getHeader("Authorization"))
                .orElse("");
        }
    }

    private static final class StringTokenFactory extends TokenFactory {
        private final String token;

        StringTokenFactory(final String token) {
            this.token = token;
        }

        @Override
        public Token create() {
            return token.isEmpty()
                ? EMPTY_TOKEN
                : new JsonToken(token);
        }
    }

    private static final class JsonToken implements Token {
        private final JsonObject json;

        JsonToken(final String json) {
            this.json = new JsonObject(json);
        }

        @Override
        public JsonObject toJson() {
            return json;
        }

//        @Override
//        public boolean valid() {
//            return true;
//        }
    }
}

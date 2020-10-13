package robhopkins.wc.iam.request;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.vertx.ext.web.RoutingContext;
import org.json.JSONObject;
import robhopkins.wc.iam.sheet.Token;
import robhopkins.wc.iam.user.domain.UserId;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@ApplicationScoped
@PreMatching
@Provider
public class TokenFilter implements ContainerRequestFilter, RequestToken {

    private static final String AUTH_PATTERN = "Bearer (.*)";

    @Override
    public void filter(final ContainerRequestContext ctx) throws IOException {
        context().put("token", token(ctx));
    }

    @Override
    public Token toToken() {
        final JSONObject json = fromContext();
        return new Token() {
            @Override
            public String toJson() {
                return json.toString(2);
            }

            @Override
            public UserId userId() {
                return UserId.from(json.get("user"));
            }
        };
    }

    private JSONObject fromContext() {
        final String token = context().get("token");
        return new JSONObject(
            new String(Base64.getDecoder().decode(token))
        );
    }

    private String token(final ContainerRequestContext ctx) {
        return Optional.ofNullable(ctx.getHeaderString("Authorization"))
            .filter(head -> head.matches(AUTH_PATTERN))
            .map(this::parse)
            .orElse("");

    }

    private String parse(final String header) {
        return header.substring("Bearer ".length()).trim();
    }

    private RoutingContext context() {
        return CDI.current().select(CurrentVertxRequest.class).get().getCurrent();
    }
}

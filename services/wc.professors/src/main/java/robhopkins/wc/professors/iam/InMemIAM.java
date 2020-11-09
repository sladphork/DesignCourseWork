package robhopkins.wc.professors.iam;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.vertx.ext.web.RoutingContext;
import robhopkins.wc.professors.iam.exception.ForbiddenException;
import robhopkins.wc.professors.iam.exception.UnauthenticatedException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Optional;

// TODO: The IAM code can probably be moved to a client so all the services
//  can use it instead of duplication.
@ApplicationScoped
@PreMatching
@Provider
public final class InMemIAM implements IAM, ContainerRequestFilter {
    private static final String AUTH_PATTERN = "Bearer (.*)";

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        context().put("token", token(ctx));
    }

    @Override
    public Token validate(final String role) throws UnauthenticatedException, ForbiddenException {
        final String json = context().get("token");
        return Optional.of(json)
            .filter(val -> val.length() > 0)
            .map(Token::from)
            .filter(t -> t.isValid(role))
            .orElseThrow(UnauthenticatedException::new);
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

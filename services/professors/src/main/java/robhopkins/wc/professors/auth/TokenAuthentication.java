package robhopkins.wc.professors.auth;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import robhopkins.wc.professors.iam.IAM;
import robhopkins.wc.professors.iam.Token;
import robhopkins.wc.professors.iam.TokenFactory;

public class TokenAuthentication implements Handler<RoutingContext> {

    private final IAM requests;
    public TokenAuthentication(final IAM requests) {
        this.requests = requests;
    }

    @Override
    public void handle(final RoutingContext ctx) {
        try {
            final Token token = token(ctx.request());
            ctx.put("token", requests.testToken(token));
            ctx.next();
        } catch (AuthenticationException ex) {
            ctx.fail(403);
        }

    }

    private Token token(final HttpServerRequest request) throws AuthenticationException {
        return TokenFactory.from(request).create();
    }
}

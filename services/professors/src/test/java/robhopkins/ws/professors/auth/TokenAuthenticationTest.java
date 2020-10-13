package robhopkins.ws.professors.auth;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import robhopkins.wc.professors.auth.TokenAuthentication;
import robhopkins.wc.professors.iam.IAM;
import robhopkins.wc.professors.iam.InMemIAM;
import robhopkins.wc.professors.iam.Token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

// TODO: Make this a bit more structured.
final class TokenAuthenticationTest {

    @Test
    void handleCallsIAMWithNoAuthHeader() throws Exception {
        final IAM requests = new InMemIAM();

        final RoutingContext ctx = Mockito.mock(RoutingContext.class);
        final HttpServerRequest request = Mockito.mock(HttpServerRequest.class);
        Mockito.when(ctx.request()).thenReturn(request);

        final TokenAuthentication auth = new TokenAuthentication(requests);
        auth.handle(ctx);

        verify(ctx).fail(eq(403));
    }

    @Test
    void handleCallsIAMWithInvalidHeader() throws Exception {
        final IAM requests = new InMemIAM();

        final RoutingContext ctx = Mockito.mock(RoutingContext.class);
        final HttpServerRequest request = Mockito.mock(HttpServerRequest.class);
        Mockito.when(request.getHeader(eq("Authorization"))).thenReturn("invalid");
        Mockito.when(ctx.request()).thenReturn(request);

        final TokenAuthentication auth = new TokenAuthentication(requests);
        auth.handle(ctx);

        verify(ctx).fail(eq(403));
    }

    @Test
    void handleCallsIAMWithInvalidBearerHeader() throws Exception {
        final IAM requests = new InMemIAM();

        final RoutingContext ctx = Mockito.mock(RoutingContext.class);
        final HttpServerRequest request = Mockito.mock(HttpServerRequest.class);
        Mockito.when(request.getHeader(eq("Authorization"))).thenReturn("Bearer");
        Mockito.when(ctx.request()).thenReturn(request);

        final TokenAuthentication auth = new TokenAuthentication(requests);
        auth.handle(ctx);

        verify(ctx).fail(eq(403));
    }

    @Test
    void handleCallsIAMWithValidBearerHeader() throws Exception {
        final IAM requests = new InMemIAM();

        final RoutingContext ctx = Mockito.mock(RoutingContext.class);
        final HttpServerRequest request = Mockito.mock(HttpServerRequest.class);
        final String token = token("test_user");
        Mockito.when(request.getHeader(eq("Authorization"))).thenReturn("Bearer " + token);
        Mockito.when(ctx.request()).thenReturn(request);
        final HttpServerResponse response = Mockito.mock(HttpServerResponse.class);
        Mockito.when(ctx.response()).thenReturn(response);

        final TokenAuthentication auth = new TokenAuthentication(requests);
        auth.handle(ctx);

        final ArgumentCaptor<Token> captor = ArgumentCaptor.forClass(Token.class);
        verify(ctx).put(eq("token"), argThat(matchesToken("test_user")));
        verify(ctx).next();

    }

    private Matcher<Token> matchesToken(final String username) {
        return new ArgumentMatcher<Token>() {
            @Override
            public boolean matches(final Object argument) {
                return username.equals(((Token)argument).toJson().getString("sub"));
            }
        };
    }

    private String token(final String username) {
        return Base64.getEncoder().encodeToString(new JsonObject()
            .put("sub", username)
            .put("role", "STUDENT")
            .put("iss", "Wylie College")
            .put("name", "Test")
            .put("exp", Instant.now().plus(1, ChronoUnit.DAYS).toString())
            .put("user", UUID.randomUUID().toString())
            .toString().getBytes());
    }
}

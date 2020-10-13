package robhopkins.wc.iam.sheet;

import org.json.JSONObject;
import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.Users;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.UserNotFoundException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

final class DefaultSigninSheet implements SigninSheet {

    private final Users users;
    private final List<Token> tokens;

    DefaultSigninSheet(final Users users) {
        this.users = users;
        this.tokens = Collections.synchronizedList(new LinkedList<>());
    }

    @Override
    public Token signin(final Credentials credentials) throws AuthenticationException {
        try {
            return newToken(authenticate(credentials));
        } catch (UserNotFoundException e) {
            throw new AuthenticationException(Username.from(credentials.username()));
        }
    }

    @Override
    public void signout(final Token token) {
        tokens.stream()
            .filter(t -> t.toJson().equals(token.toJson()))
            .findFirst()
            .ifPresent(tokens::remove);

    }

    private User authenticate(final Credentials credentials) throws UserNotFoundException, AuthenticationException {
        final User user = users.get(Username.from(credentials.username()));
        user.authenticate(credentials.secret());
        return user;
    }

    private Token newToken(final User user) {
        final Token token = new DefaultToken(user);
        tokens.add(token);
        return token;
    }

    private static final class DefaultToken implements Token {

        private final UserId id;
        private final JSONObject json;

        DefaultToken(final User user) {
            this.id = user.id();
            json = new JSONObject()
                .put("iss", "Wylie College")
                .put("sub", user.username().toString())
                .put("name", user.name().toString())
                .put("role", user.role().toString())
                .put("user", id.toString())
                .put("exp", Instant.now().plus(1, ChronoUnit.DAYS));
        }

        @Override
        public String toJson() {
            return json.toString(2);
        }

        @Override
        public UserId userId() {
            return id;
        }
    }
}

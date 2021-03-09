package robhopkins.wc.iam.request;

import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.sheet.Credentials;
import robhopkins.wc.iam.sheet.SigninSheet;
import robhopkins.wc.iam.sheet.Token;
import robhopkins.wc.iam.user.Users;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;

@ApplicationScoped
public final class RequestSigninSheet implements SigninSheet  {

    @Inject
    Users users;

    private SigninSheet target;

    public RequestSigninSheet() {
    }

    @Override
    public Token signin(final Credentials credentials) throws AuthenticationException {
        return target().signin(credentials);
    }

    @Override
    public void signout(final Token token) {
        target().signout(token);
    }

    private synchronized SigninSheet target() {
        if (Objects.isNull(target)) {
            target = SigninSheet.newSheet(users);
        }
        return target;
    }
}

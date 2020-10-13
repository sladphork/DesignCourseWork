package robhopkins.wc.iam.request;

import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.sheet.Credentials;
import robhopkins.wc.iam.sheet.SigninSheet;
import robhopkins.wc.iam.sheet.Token;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class RequestSigninSheet implements SigninSheet  {

    private final SigninSheet target;

    public RequestSigninSheet() {
        target = SigninSheet.newSheet();
    }

    @Override
    public Token signin(final Credentials credentials) throws AuthenticationException {
        return target.signin(credentials);
    }

    @Override
    public void signout(final Token token) {
        target.signout(token);
    }
}

package robhopkins.wc.iam.sheet;

import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.user.UsersFactory;

public interface SigninSheet {

    static SigninSheet newSheet() {
        return new DefaultSigninSheet(
            UsersFactory.newFactory().create()
        );
    }

    Token signin(Credentials credentials) throws AuthenticationException;

    // TODO: Change to throw exception?
    void signout(Token token);
}

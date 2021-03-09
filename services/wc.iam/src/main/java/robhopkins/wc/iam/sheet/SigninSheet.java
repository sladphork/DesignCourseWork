package robhopkins.wc.iam.sheet;

import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.user.Users;
import robhopkins.wc.iam.user.UsersFactory;

public interface SigninSheet {

    static SigninSheet newSheet(final Users users) {
        return new DefaultSigninSheet(users);
    }

    Token signin(Credentials credentials) throws AuthenticationException;

    // TODO: Change to throw exception?
    void signout(Token token);
}

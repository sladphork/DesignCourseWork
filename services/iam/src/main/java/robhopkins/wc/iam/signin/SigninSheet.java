package robhopkins.wc.iam.signin;


import robhopkins.wc.iam.user.UsersFactory;
import robhopkins.wc.iam.user.exception.AuthenticationException;

public abstract class SigninSheet {

    public static SigninSheet newSheet() {
        return new DefaultSigninSheet(
            UsersFactory.newFactory().create()
        );
    }

    public abstract Token signin(Credentials credentials) throws AuthenticationException;

    // TODO: Change to throw exception?
    public abstract void signout(Token token);
}

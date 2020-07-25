package robhopkins.wc.iam.user;

import org.json.JSONObject;
import robhopkins.wc.iam.user.domain.Email;
import robhopkins.wc.iam.user.domain.Name;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.AuthenticationException;

import java.util.Arrays;

abstract class AbstractUser implements User {

    private final JSONObject json;

    AbstractUser(final JSONObject json) {
        this.json = json;
    }

    @Override
    public final UserId id() {
        return UserId.from(json.getString("id"));
    }

    @Override
    public final Username username() {
        return Username.from(json.getString("username"));
    }

    @Override
    public final Name name() {
        return Name.from(json.getString("first_name"), json.getString("last_name"));
    }

    @Override
    public final Email email() {
        return Email.from(json.getString("email"));
    }

    @Override
    public void authenticate(final byte[] secret) throws AuthenticationException {
        // For now, we will just use "password" as the secret.
        if (!Arrays.equals("password".getBytes(), secret)) {
            throw new AuthenticationException(username());
        }
    }
}

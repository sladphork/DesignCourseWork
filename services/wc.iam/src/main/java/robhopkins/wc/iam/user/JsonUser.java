package robhopkins.wc.iam.user;

import org.json.JSONObject;
import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.user.domain.*;

final class JsonUser implements User {

    private final JSONObject json;
    JsonUser(final JSONObject json) {
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
        return Name.from(json.getString("firstName"), json.getString("lastName"));
    }

    @Override
    public final Email email() {
        return Email.from(json.getString("email"));
    }

    @Override
    public Role role() {
        return Role.valueOf(json.getString("role").toUpperCase());
    }

    @Override
    public void authenticate(final byte[] toCheck) throws AuthenticationException {
        Secrets.newInstance().getFor(this).check(toCheck);
    }
}

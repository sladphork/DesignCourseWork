package robhopkins.wc.iam.user;

import org.json.JSONObject;
import robhopkins.wc.iam.user.domain.Role;

public final class UserBuilder {
    public static UserBuilder newBuilder() {
        return new UserBuilder(new JSONObject());
    }

    public static UserBuilder newBuilder(final JSONObject json) {
        return new UserBuilder(json);
    }

    private final JSONObject json;
    private UserBuilder(final JSONObject json) {
        this.json = json;
    }

    public UserBuilder withId(final String id) {
        json.put("id", id);
        return this;
    }

    public UserBuilder withFirstName(final String firstName) {
        json.put("firstName", firstName);
        return this;
    }

    public UserBuilder withLastName(final String lastName) {
        json.put("lastName", lastName);
        return this;
    }

    public UserBuilder withUsername(final String username) {
        json.put("username", username);
        return this;
    }

    public UserBuilder withEmail(final String email) {
        json.put("email", email);
        return this;
    }

    public UserBuilder withRole(final Role role) {
        json.put("role", role.toString());
        return this;
    }

    public User build() {
        return new JsonUser(json);
    }
}

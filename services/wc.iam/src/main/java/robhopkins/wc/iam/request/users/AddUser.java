package robhopkins.wc.iam.request.users;

import org.json.JSONObject;
import robhopkins.wc.iam.request.Payload;
import robhopkins.wc.iam.request.exception.IAMException;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.UserBuilder;
import robhopkins.wc.iam.user.domain.Role;

import java.util.Objects;
import java.util.UUID;


public final class AddUser {

    public static AddUser from(final String json) throws IAMException {
        final AddUser addUser = Payload.newPayload(AddUser.class)
            .withJSON(new JSONObject(json))
            .withSchema("firstName", "lastName", "role", "email:false", "username:false")
            .build();
        addUser.validateRole();
        return addUser;
    }

    private final JSONObject json;

    AddUser(final JSONObject json) {
        this.json = json;
    }

    public User toUser() {
        return UserBuilder.newBuilder(json)
            .withId(UUID.randomUUID().toString())
            .withUsername(username())
            .withEmail(email())
            .build();
    }

    private String username() {
        final String username = value("username");
        return Objects.nonNull(username)
            ? username
            : (json.getString("firstName").charAt(0) + json.getString("lastName")).toLowerCase();
    }

    private String email() {
        // TODO: Generate based on the role.
        final String email = value("email");
        return Objects.nonNull(email)
            ? email
            : json.getString("firstName") + "." + json.getString("lastName") + "@wc.edu";
    }

    private String value(final String field) {
        return json.has(field) && !json.getString(field).isBlank()
            ? json.getString(field)
            : null;
    }

    private void validateRole() throws IAMException {
        final String role = json.getString("role");
        try {
            Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException(role);
        }
    }

    private static final class InvalidRoleException extends IAMException {
        InvalidRoleException(final String role) {
            super(
                String.format("Invalid Role defined: '%s'", role)
            );
        }

        @Override
        public int status() {
            return 400;
        }
    }
}

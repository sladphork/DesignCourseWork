package robhopkins.wc.iam.user;

import org.json.JSONObject;
import robhopkins.wc.iam.user.domain.Role;

import java.io.InputStream;

final class RegistrarUsers extends AbstractUsers {

    RegistrarUsers(final User... users) {
        super(users);
    }

    @Override
    InputStream jsonInput() {
        return getClass().getResourceAsStream("/registrars.json");
    }

    @Override
    User fromJson(final JSONObject json) {
        return new Registrar(json);
    }

    private static final class Registrar extends AbstractUser {

        Registrar(final JSONObject json) {
            super(json);
        }

        @Override
        public Role role() {
            return Role.REGISTRAR;
        }
    }
}

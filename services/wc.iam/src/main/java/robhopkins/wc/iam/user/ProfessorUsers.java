package robhopkins.wc.iam.user;

import org.json.JSONObject;
import robhopkins.wc.iam.user.domain.Role;

import java.io.InputStream;

final class ProfessorUsers extends AbstractUsers{

    ProfessorUsers(final User... users) {
        super(users);
    }

    @Override
    InputStream jsonInput() {
        return getClass().getResourceAsStream("/professors.json");
    }

    @Override
    User fromJson(final JSONObject json) {
        return new ProfessorUser(json);
    }

    private static final class ProfessorUser extends AbstractUser {

        ProfessorUser(final JSONObject json) {
            super(json);
        }

        @Override
        public Role role() {
            return Role.PROFESSOR;
        }
    }
}

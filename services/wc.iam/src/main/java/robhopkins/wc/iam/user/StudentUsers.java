package robhopkins.wc.iam.user;

import org.json.JSONObject;
import robhopkins.wc.iam.user.domain.Role;

import java.io.InputStream;

final class StudentUsers extends AbstractUsers {

    StudentUsers(final User... users) {
        super(users);
    }

    @Override
    InputStream jsonInput() {
        return getClass().getResourceAsStream("/students.json");
    }

    @Override
    User fromJson(final JSONObject json) {
        return new StudentUser(json);
    }

    private static final class StudentUser extends AbstractUser {
        StudentUser(final JSONObject json) {
            super(json);
        }

        @Override
        public Role role() {
            return Role.STUDENT;
        }
    }

}

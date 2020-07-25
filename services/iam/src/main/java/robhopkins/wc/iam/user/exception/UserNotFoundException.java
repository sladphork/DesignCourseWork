package robhopkins.wc.iam.user.exception;

import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(final UserId id) {
        super(String.format("User with ID: '%s' not found.", id.toString()));
    }

    public UserNotFoundException(final Username username) {
        super(String.format("User with name: '%s' not found.", username.toString()));
    }
}

package robhopkins.wc.iam.user.exception;

import robhopkins.wc.iam.rest.exception.IAMException;
import robhopkins.wc.iam.user.domain.Username;

public class AuthenticationException extends IAMException {
    public AuthenticationException(final Username username) {
        super(String.format("Authentication failed for: '%s'", username));
    }

    @Override
    protected int status() {
        return 401;
    }
}

package robhopkins.wc.iam.request.exception;

import robhopkins.wc.iam.user.domain.Username;

public class AuthenticationException extends IAMException {
    public AuthenticationException(final Username username) {
        this(String.format("Authentication failed for: '%s'", username));
    }

    public AuthenticationException(final String message) {
        super(message);
    }

    @Override
    public int status() {
        return 401;
    }
}

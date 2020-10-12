package robhopkins.wc.iam.request.exception;

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

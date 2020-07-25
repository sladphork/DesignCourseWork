package robhopkins.wc.iam.rest.exception;

import org.takes.Response;
import robhopkins.wc.iam.rest.ErrorResponseBuilder;

public abstract class IAMException extends Exception {
    protected IAMException(final String message) {
        super(message);
    }

    protected abstract int status();

    public final Response toResponse() {
        return ErrorResponseBuilder.newBuilder()
            .withStatus(status())
            .withMessage(getMessage())
            .build();
    }
}

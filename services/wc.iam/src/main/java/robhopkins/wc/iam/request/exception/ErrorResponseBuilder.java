package robhopkins.wc.iam.request.exception;

import javax.ws.rs.core.Response;
import java.util.Optional;

public final class ErrorResponseBuilder {

    public static ErrorResponseBuilder newBuilder() {
        return new ErrorResponseBuilder("application/vnd.wc.error.v1+json");
    }

    private final String contentType;
    private int status = 400;
    private String message;

    private ErrorResponseBuilder(final String contentType) {
        this.contentType = contentType;
    }

    public ErrorResponseBuilder withStatus(final int status) {
        this.status = status;
        return this;
    }

    public ErrorResponseBuilder withMessage(final String message) {
        this.message = message;
        return this;
    }

    public Response build() {
        return Response.status(status)
            .header("Content-type", contentType)
            .entity(Optional.ofNullable(message).orElse(""))
            .build();
    }
}

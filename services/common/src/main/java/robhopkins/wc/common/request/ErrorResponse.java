package robhopkins.wc.common.request;

import robhopkins.wc.common.request.exception.RequestException;

public final class ErrorResponse {
    public static ErrorResponse from(final RequestException ex) {
        return new ErrorResponse(ex);
    }

    private final RequestException ex;
    private ErrorResponse(final RequestException ex) {
        this.ex = ex;
    }

    public void populate(final ErrorMessage message) {
        message.status(ex.status());
        message.message(ex.getMessage());
        message.contentType("application/vnd.wc.error.v1+text");
    }

    public interface ErrorMessage {
        void status(int status);
        void message(String message);
        void contentType(String contentType);
    }
}

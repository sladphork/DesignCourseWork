package robhopkins.wc.common.request.exception;

public abstract class RequestException extends Exception {
    public RequestException() {
    }

    public RequestException(final String message) {
        super(message);
    }

    public abstract int status();
}

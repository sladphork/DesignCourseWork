package robhopkins.wc.iam.request.exception;

public abstract class IAMException extends Exception {
    protected IAMException(final String message) {
        super(message);
    }

    public abstract int status();
}

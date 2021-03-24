package robhopkins.wc.students.exception;

public class ServerException extends StudentException {
    public ServerException(final String message) {
        super(message);
    }

    public ServerException(final Exception cause) {
        super(cause.getMessage());
    }

    @Override
    public int status() {
        return 500;
    }
}

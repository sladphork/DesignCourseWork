package robhopkins.wc.professors.exception;

public class ServerException  extends ProfessorException {

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

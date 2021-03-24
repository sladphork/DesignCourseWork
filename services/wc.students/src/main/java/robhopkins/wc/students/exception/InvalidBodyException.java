package robhopkins.wc.students.exception;

public class InvalidBodyException extends StudentException {

    public InvalidBodyException(final String type, final String... fields) {
        super(
            String.format("Type: '%s' has invalid fields: [%s]", type, String.join(",", fields))
        );
    }

    @Override
    protected int status() {
        return 400;
    }
}

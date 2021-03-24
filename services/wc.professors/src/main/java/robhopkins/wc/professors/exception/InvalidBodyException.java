package robhopkins.wc.professors.exception;

public class InvalidBodyException extends ProfessorException {

    public InvalidBodyException(final String type, final String... fields) {
        super(
            String.format("Type: '%s' has invalid fields: [%s]", type, String.join(",", fields))
        );
    }

    @Override
    public int status() {
        return 400;
    }
}

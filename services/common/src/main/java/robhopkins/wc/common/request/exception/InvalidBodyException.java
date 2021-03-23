package robhopkins.wc.common.request.exception;

public class InvalidBodyException extends RequestException {

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

package robhopkins.wc.iam.rest.exception;

public class BadRequest extends IAMException {

    public BadRequest(final String message) {
        super(message);
    }

    @Override
    protected int status() {
        return 400;
    }
}

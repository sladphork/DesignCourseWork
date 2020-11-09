package robhopkins.wc.professors.exception;

import javax.ws.rs.core.Response;

public abstract class ProfessorException extends Exception {
    public ProfessorException() {
    }

    public ProfessorException(final String message) {
        super(message);
    }

    public Response toResponse() {
        return Response.status(status())
            .header("Content-type", "application/vnd.wc.error.v1+text")
            .entity(getMessage())
            .build();
    }

    protected abstract int status();
}

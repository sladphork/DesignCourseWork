package robhopkins.wc.professors.iam.exception;

import robhopkins.wc.professors.exception.ProfessorException;

public abstract class IAMException extends ProfessorException {
    IAMException(final String message) {
        super(message);
    }
}

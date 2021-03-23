package robhopkins.wc.students.iam.exception;

import robhopkins.wc.students.exception.StudentException;

public abstract class IAMException extends StudentException {
    IAMException(final String message) {
        super(message);
    }
}

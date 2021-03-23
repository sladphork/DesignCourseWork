package robhopkins.wc.students.faculty.exception;

import robhopkins.wc.students.exception.StudentException;

public final class FacultyNotFoundException extends StudentException {

    // TODO: Figure out what the id type will be.
    public FacultyNotFoundException(final String id) {
        super(String.format("Unable to find faculty: '%s'", id));
    }

    @Override
    protected int status() {
        return 404;
    }
}

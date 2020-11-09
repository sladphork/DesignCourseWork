package robhopkins.wc.professors.faculty.exception;

import robhopkins.wc.professors.exception.ProfessorException;

public final class FacultyNotFoundException extends ProfessorException {

    // TODO: Figure out what the id type will be.
    public FacultyNotFoundException(final String id) {
        super(String.format("Unable to find faculty: '%s'", id));
    }

    @Override
    protected int status() {
        return 404;
    }
}

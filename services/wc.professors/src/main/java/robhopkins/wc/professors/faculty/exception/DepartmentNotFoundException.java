package robhopkins.wc.professors.faculty.exception;

import robhopkins.wc.professors.exception.ProfessorException;

public class DepartmentNotFoundException extends ProfessorException {

    // TODO: Figure out what the id type will be.
    public DepartmentNotFoundException(final String id) {
        super(String.format("Unable to find department: '%s'", id));
    }

    @Override
    public int status() {
        return 404;
    }
}
package robhopkins.wc.professors.exception;

import robhopkins.wc.professors.domain.ObjectId;

public final class ProfessorNotFoundException extends ProfessorException {

    public ProfessorNotFoundException(final ObjectId id) {
        super(String.format("Unable to find professor: '%s'", id.toString()));
    }

    @Override
    protected int status() {
        return 404;
    }
}

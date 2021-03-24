package robhopkins.wc.students.exception;


import robhopkins.wc.students.domain.ObjectId;

public final class StudentNotFoundException extends StudentException {

    public StudentNotFoundException(final ObjectId id) {
        super(String.format("Unable to find professor: '%s'", id.toString()));
    }

    @Override
    protected int status() {
        return 404;
    }
}

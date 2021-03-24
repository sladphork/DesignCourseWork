package robhopkins.wc.students.faculty;

import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.faculty.exception.FacultyNotFoundException;

public final class FacultiesFactory {
    public static FacultiesFactory newFactory() {
        return new FacultiesFactory();
    }

    private FacultiesFactory() {
    }

    public Faculties create() {
        // We can control this via config, etc.
        return new InMemFaculties();
    }

    private static final class InMemFaculties implements Faculties {
        @Override
        public void check(final ObjectId facultyId) throws FacultyNotFoundException {
            // Do nothing, since we are good with anything.
            // TODO: Eventually add a call to the faculties service to check if exists.
        }
    }
}

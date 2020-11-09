package robhopkins.wc.professors.faculty;

import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.faculty.exception.FacultyNotFoundException;

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
            // TODO: load the csv to map ids to names.
        }
    }
}

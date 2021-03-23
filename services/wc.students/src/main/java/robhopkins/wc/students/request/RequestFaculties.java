package robhopkins.wc.students.request;

import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.faculty.Faculties;
import robhopkins.wc.students.faculty.FacultiesFactory;
import robhopkins.wc.students.faculty.exception.FacultyNotFoundException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class RequestFaculties implements Faculties {

    private final Faculties target;
    public RequestFaculties() {
        target = FacultiesFactory.newFactory().create();
    }

    @Override
    public void check(final ObjectId facultyId) throws FacultyNotFoundException {
        target.check(facultyId);
    }
}

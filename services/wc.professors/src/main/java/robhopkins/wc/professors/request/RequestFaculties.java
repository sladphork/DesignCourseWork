package robhopkins.wc.professors.request;

import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.faculty.Faculties;
import robhopkins.wc.professors.faculty.FacultiesFactory;
import robhopkins.wc.professors.faculty.exception.DepartmentNotFoundException;
import robhopkins.wc.professors.faculty.exception.FacultyNotFoundException;

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

    @Override
    public void checkDepartment(ObjectId departmentId) throws DepartmentNotFoundException {
        target.checkDepartment(departmentId);
    }
}

package robhopkins.wc.professors.faculty;

import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.faculty.exception.FacultyNotFoundException;

public interface Faculties {

    void check(ObjectId facultyId) throws FacultyNotFoundException;
}

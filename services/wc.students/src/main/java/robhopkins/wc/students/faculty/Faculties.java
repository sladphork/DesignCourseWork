package robhopkins.wc.students.faculty;

import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.faculty.exception.FacultyNotFoundException;

public interface Faculties {

    void check(ObjectId facultyId) throws FacultyNotFoundException;
}

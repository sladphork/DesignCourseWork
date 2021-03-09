package robhopkins.wc.professors.request;

import io.quarkus.test.Mock;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.faculty.Faculties;
import robhopkins.wc.professors.faculty.exception.DepartmentNotFoundException;
import robhopkins.wc.professors.faculty.exception.FacultyNotFoundException;

import java.util.Objects;

@Mock
final class MockFaculties implements Faculties {

    private static FacultyNotFoundException notFoundException;
    private static DepartmentNotFoundException deptNotFoundException;

    static void throwNotFound(final FacultyNotFoundException notFoundException) {
        MockFaculties.notFoundException = notFoundException;
    }

    static void throwNotFound(final DepartmentNotFoundException notFoundException) {
        MockFaculties.deptNotFoundException = notFoundException;
    }

    static void clear() {
        notFoundException = null;
        deptNotFoundException = null;
    }

    @Override
    public void check(final ObjectId facultyId) throws FacultyNotFoundException {
        if (Objects.nonNull(notFoundException)) {
            throw notFoundException;
        }
    }

    @Override
    public void checkDepartment(final ObjectId departmentId) throws DepartmentNotFoundException {
        if (Objects.nonNull(deptNotFoundException)) {
            throw deptNotFoundException;
        }
    }
}

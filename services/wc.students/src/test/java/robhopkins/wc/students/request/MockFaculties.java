package robhopkins.wc.students.request;

import io.quarkus.test.Mock;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.faculty.Faculties;
import robhopkins.wc.students.faculty.exception.FacultyNotFoundException;

import java.util.Objects;

@Mock
final class MockFaculties implements Faculties {

    private static FacultyNotFoundException notFoundException;

    static void throwNotFound(final FacultyNotFoundException notFoundException) {
        MockFaculties.notFoundException = notFoundException;
    }

    static void clear() {
        notFoundException = null;
    }

    @Override
    public void check(final ObjectId facultyId) throws FacultyNotFoundException {
        if (Objects.nonNull(notFoundException)) {
            throw notFoundException;
        }
    }
}

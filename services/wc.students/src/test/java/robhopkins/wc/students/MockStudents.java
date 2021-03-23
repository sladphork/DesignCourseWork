package robhopkins.wc.students;

import io.quarkus.test.Mock;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.ServerException;
import robhopkins.wc.students.exception.StudentException;
import robhopkins.wc.students.exception.StudentNotFoundException;

import java.util.Collection;
import java.util.Objects;

@Mock
public final class MockStudents implements Students {

    // Note that I'm not a true decorator because
    //  I'm a garbage solution to Quarkus creating multiple mocks.
    private static final InMemStudents target = new InMemStudents();

    public static void put(final Student... students) {
        target.put(students);
    }

    private static StudentException exception;

    public static void throwNotFoundException(final StudentNotFoundException notFoundException) {
        exception = notFoundException;
    }

    public static void clear() {
        exception = null;
        target.clear();
    }

    @Override
    public Student get(final ObjectId id) throws StudentNotFoundException, ServerException {
        if (Objects.nonNull(exception)) {
            throw (StudentNotFoundException) exception;
        }
        return target.get(id);
    }

    @Override
    public Collection<Student> getAll() throws ServerException {
        return target.getAll();
    }

    @Override
    public Student add(final Student student) throws ServerException {
        return target.add(student);
    }

    @Override
    public Student update(final Student student) throws StudentNotFoundException, ServerException {
        return target.update(student);
    }

    @Override
    public void delete(final ObjectId id) throws ServerException {
        target.delete(id);
    }
}

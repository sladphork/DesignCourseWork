package robhopkins.wc.professors;

import io.quarkus.test.Mock;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorException;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;
import robhopkins.wc.professors.exception.ServerException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Mock
public final class MockProfessors implements Professors {

    // Note that I'm not a true decorator because
    //  I'm a garbage solution to Quarkus creating multiple mocks.
    private static final InMemProfessors target = new InMemProfessors();

    public static void put(final Professor... professors) {
        target.put(professors);
    }

    private static ProfessorException exception;

    public static void throwNotFoundException(final ProfessorNotFoundException notFoundException) {
        exception = notFoundException;
    }

    public static void clear() {
        exception = null;
        target.clear();
    }

    @Override
    public Professor get(final ObjectId id) throws ProfessorNotFoundException, ServerException {
        if (Objects.nonNull(exception)) {
            throw (ProfessorNotFoundException) exception;
        }
        return target.get(id);
    }

    @Override
    public Professor add(final Professor professor) throws ServerException {
        return target.add(professor);
    }

    @Override
    public Professor update(final Professor professor) throws ProfessorNotFoundException, ServerException {
        return target.update(professor);
    }

    @Override
    public void delete(final ObjectId id) throws ServerException {
        target.delete(id);
    }

    @Override
    public Collection<Professor> getAll() throws ServerException {
        return target.getAll();
    }
}

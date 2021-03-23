package robhopkins.wc.students.db;

import robhopkins.wc.students.Student;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.db.operation.OperationFactory;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.ServerException;
import robhopkins.wc.students.exception.StudentNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Objects.isNull;

public final class DatasourceStudents implements Students {
    
    private final DatasourceService datasourceService;
    private OperationFactory factory;

    public DatasourceStudents(final DatasourceService dsService) {
        this.datasourceService = dsService;
    }

    @Override
    public Student get(final ObjectId id) throws StudentNotFoundException, ServerException {

        final Collection<Student> found = factory().forGet(id).execute();
        if (found.size() > 0) {
            return new ArrayList<>(found).get(0);
        }
        throw new StudentNotFoundException(id);
    }

    @Override
    public Collection<Student> getAll() throws ServerException {
        return factory().forGetAll().execute();
    }

    @Override
    public Student add(final Student student) throws ServerException {
        factory().forAdd(student).execute();
        return student;
    }

    @Override
    public Student update(final Student student) throws StudentNotFoundException, ServerException {
        factory().forUpdate(student, get(student.id())).execute();
        return get(student.id());
    }

    @Override
    public void delete(final ObjectId id) throws ServerException {
        // We're not going to check if prof exists, we don't really care.
        factory().forDelete(id).execute();
    }

    private synchronized OperationFactory factory() {
        if (isNull(factory)) {
            factory = OperationFactory.newFactory(datasourceService);
        }
        return factory;
    }
}

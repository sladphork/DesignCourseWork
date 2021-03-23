package robhopkins.wc.students.db.operation;

import robhopkins.wc.students.Student;
import robhopkins.wc.students.db.DatasourceService;
import robhopkins.wc.students.domain.ObjectId;

import java.util.Collection;

public final class OperationFactory {

    public static OperationFactory newFactory(final DatasourceService service) {
        return new OperationFactory(service);
    }

    private final DatasourceService service;

    private OperationFactory(final DatasourceService service) {
        this.service = service;
    }

    public Operation<Collection<Student>> forGet(final ObjectId studentId) {
        return new GetOperation(studentId, service);
    }

    public Operation<Collection<Student>> forGetAll() {
        return forGet(null);
    }

    public Operation<String> forAdd(final Student studentToAdd) {
        return new AddOperation(studentToAdd, service);
    }

    public Operation<String> forUpdate(final Student toUpdate, final Student original) {
        return new UpdateOperation(toUpdate, original, service);
    }

    public Operation<String> forDelete(final ObjectId studentId) {
        return new DeleteOperation(studentId, service);
    }
}

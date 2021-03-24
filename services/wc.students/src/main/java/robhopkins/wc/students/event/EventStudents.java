package robhopkins.wc.students.event;

import robhopkins.wc.students.Student;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.ServerException;
import robhopkins.wc.students.exception.StudentNotFoundException;

import java.util.Collection;

public class EventStudents implements Students {

    private final Students target;

    public EventStudents(final Students target) {
        this.target = target;
    }

    @Override
    public Student get(final ObjectId id) throws StudentNotFoundException, ServerException {
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

package robhopkins.wc.students;

import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.ServerException;
import robhopkins.wc.students.exception.StudentNotFoundException;

import java.util.Collection;

public interface Students {
    Student get(ObjectId id) throws StudentNotFoundException, ServerException;

    // TODO: Add support for paging?
    Collection<Student> getAll() throws ServerException;

    Student add(Student student) throws ServerException;

    Student update(Student student) throws StudentNotFoundException, ServerException;

    void delete(ObjectId id) throws ServerException;
}

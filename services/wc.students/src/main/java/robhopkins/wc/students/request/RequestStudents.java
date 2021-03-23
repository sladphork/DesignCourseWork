package robhopkins.wc.students.request;

import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.StudentsFactory;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.ServerException;
import robhopkins.wc.students.exception.StudentNotFoundException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;

@Startup
@ApplicationScoped
public final class RequestStudents implements Students {

    @ConfigProperty(name = "wc.datasource.url")
    String datasourceUrl;

    private Students target;

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

    @PostConstruct
    void start() {
        target = StudentsFactory.newFactory(datasourceUrl).create();
    }
}

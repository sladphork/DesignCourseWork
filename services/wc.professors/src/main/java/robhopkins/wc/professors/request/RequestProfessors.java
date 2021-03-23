package robhopkins.wc.professors.request;

import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.ProfessorsFactory;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;
import robhopkins.wc.professors.exception.ServerException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;

@Startup
@ApplicationScoped
public final class RequestProfessors implements Professors {

    @ConfigProperty(name = "wc.datasource.url")
    String datasourceUrl;

    private Professors target;

    @Override
    public Professor get(final ObjectId id) throws ProfessorNotFoundException, ServerException {
        return target.get(id);
    }

    @Override
    public Collection<Professor> getAll() throws ServerException {
        return target.getAll();
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

    @PostConstruct
    void start() {
        target = ProfessorsFactory.newFactory().create(datasourceUrl);
    }
}

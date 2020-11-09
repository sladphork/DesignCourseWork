package robhopkins.wc.professors.request;

import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.ProfessorsFactory;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;

@ApplicationScoped
public final class RequestProfessors implements Professors {

    private final Professors target;

    public RequestProfessors() {
        target = ProfessorsFactory.newFactory().create();
    }

    @Override
    public Professor get(final ObjectId id) throws ProfessorNotFoundException {
        return target.get(id);
    }

    @Override
    public Collection<Professor> getAll() {
        return target.getAll();
    }

    @Override
    public Professor add(final Professor professor) {
        return target.add(professor);
    }

    @Override
    public Professor update(final Professor professor) throws ProfessorNotFoundException {
        return target.update(professor);
    }

    @Override
    public void delete(final ObjectId id) {
        target.delete(id);
    }
}

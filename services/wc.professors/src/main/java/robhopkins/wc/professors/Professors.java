package robhopkins.wc.professors;

import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;

import java.util.Collection;

public interface Professors {
    Professor get(ObjectId id) throws ProfessorNotFoundException;

    // TODO: Add support for paging?
    Collection<Professor> getAll();

    Professor add(Professor professor);

    Professor update(Professor professor) throws ProfessorNotFoundException;

    void delete(ObjectId id);
}

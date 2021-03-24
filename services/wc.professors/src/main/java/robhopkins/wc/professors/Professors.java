package robhopkins.wc.professors;

import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;
import robhopkins.wc.professors.exception.ServerException;

import java.util.Collection;
import java.util.Map;

public interface Professors {
    Professor get(ObjectId id) throws ProfessorNotFoundException, ServerException;

    // TODO: Add support for paging?
    Collection<Professor> getAll() throws ServerException;

    Professor add(Professor professor) throws ServerException;;

    Professor update(Professor professor) throws ProfessorNotFoundException, ServerException;

    void delete(ObjectId id) throws ServerException;
}

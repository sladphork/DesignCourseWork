package robhopkins.wc.professors.db;

import org.jboss.logmanager.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;
import robhopkins.wc.common.datasource.*;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.ProfessorBuilder;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;
import robhopkins.wc.professors.exception.ServerException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class DatasourceProfessors implements Professors {

    private final DatasourceAction action;

    public DatasourceProfessors(final DatasourceAction action) {
        this.action = action;
    }

    @Override
    public Professor get(final ObjectId id) throws ProfessorNotFoundException, ServerException {
        final DatasourceResponse response = execute(
            QueryRequestBuilder.newBuilder()
                .withNamespace("professors")
                .forTable("professors")
                .where("id", id)
        );
        final String professors = response.body();
        final JSONArray result = new JSONArray(professors);
        if (result.length() > 0) {
            return mapFromDB(result.get(0));
        }
        throw new ProfessorNotFoundException(id);
    }

    @Override
    public Collection<Professor> getAll() throws ServerException {
        final DatasourceResponse response = execute(
            QueryRequestBuilder.newBuilder()
                .withNamespace("professors")
                .forTable("professors")
        );
        final String professors = response.body();
        final JSONArray result = new JSONArray(professors);
        return StreamSupport.stream(result.spliterator(), false)
            .map(this::mapFromDB)
            .collect(Collectors.toList());
    }

    private Professor mapFromDB(final Object val) {
        final JSONObject obj = (JSONObject) val;
        return ProfessorBuilder.newBuilder()
            .withDepartmentId(ObjectId.from(obj.getString("DEPARTMENT_ID")))
            .withFirstName(obj.getString("FIRST_NAME"))
            .withLastName(obj.getString("LAST_NAME"))
            .withId(ObjectId.from(obj.getString("ID")))
            .withEmail(obj.getString("EMAIL"))
            .build();
    }

    @Override
    public Professor add(final Professor professor) throws ServerException {
        final DatasourceResponse response = execute(
            InsertRequestBuilder.newBuilder()
            .withNamespace("professors")
            .forTable("professors")
            .withValues(DMLPopulator.forAdd(professor).toMap())
        );
        LogManager.getLogManager().getLogger("DatasourceProfessors")
            .info("\nAdded professor with response: " + response.body());
        return professor;
    }

    @Override
    public Professor update(final Professor professor) throws ProfessorNotFoundException, ServerException {
        final DatasourceResponse response = execute(
            UpdateRequestBuilder.newBuilder()
                .withNamespace("professors")
                .forTable("professors")
                .forId("id", professor.id())
                .withValues(DMLPopulator.forUpdate(professor, setTests(get(professor.id()))).toMap())
        );
        LogManager.getLogManager().getLogger("DatasourceProfessors")
            .info("\nAdded professor with response: " + response.body());
        return get(professor.id());
    }

    private Map<String, Predicate<Object>> setTests(final Professor professor) {
        final Map<String, Object> original = DMLPopulator.forProfessor(professor).toMap();
        return Map.of(
            "first_name", updatePredicate("first_name", original),
            "last_name", updatePredicate("last_name", original),
            "department_id", updatePredicate("department_id", original)
        );
    }

    private Predicate<Object> updatePredicate(final String name, final Map<String, Object> map) {
        return value -> {
            final Object oldValue = map.get(name);
            return (!Objects.toString(value, "").isBlank()
                && !Objects.equals(oldValue, value));
        };
    }

    @Override
    public void delete(final ObjectId id) throws ServerException {
        // We're not going to check if prof exists, we don't really care.
        execute(
            DeleteRequestBuilder.newBuilder()
                .withNamespace("professors")
                .forTable("professors")
                .where("id", id)
        );
    }

    private DatasourceResponse execute(final DatasourceRequestBuilder builder) throws ServerException {
        return action.execute(builder);
    }
}

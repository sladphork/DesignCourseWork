package robhopkins.wc.professors.db;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jboss.logmanager.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.ProfessorBuilder;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class DatasourceProfessors implements Professors {
    private final DatasourceRequest request;
    public DatasourceProfessors(final DatasourceRequest request) {
        this.request = request;
    }

    @Override
    public void configure(final Map<String, Object> properties) {
        RestAssured.baseURI = (String) properties.get("dburl");
    }

    @Override
    public Professor get(final ObjectId id) throws ProfessorNotFoundException {
        final JSONObject request = new JSONObject()
            .put("namespace", "professors")
            .put("options", queryOptions("id", id))
            .put("type", "QUERY");

        final Response response = execute(request);
        final String professors = response.asString();
        final JSONArray result = new JSONArray(professors);
        if (result.length() > 0) {
            return mapFromDB(result.get(0));
        }
        throw new ProfessorNotFoundException(id);
    }

    @Override
    public Collection<Professor> getAll() {
        final JSONObject request = new JSONObject()
            .put("namespace", "professors")
            .put("options", queryOptions(null, null))
            .put("type", "QUERY");

        final Response response = execute(request);
        final String professors = response.asString();
        final JSONArray result = new JSONArray(professors);
        return StreamSupport.stream(result.spliterator(), false)
            .map(this::mapFromDB)
            .collect(Collectors.toList());
    }

    private JSONObject queryOptions(final String field, final Object value) {
        final JSONObject options = new JSONObject()
            .put("table", "professors");
        if (Objects.nonNull(field)) {
            final JSONObject where = new JSONObject()
                .put("field", field)
                .put("value", value.toString());
            options.put("where", where);
        }
        return options;
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
    public Professor add(final Professor professor) {
        final JSONObject request = new JSONObject()
            .put("namespace", "professors")
            .put("options", options(professor))
            .put("type", "INSERT");

        final Response response = execute(request);
        LogManager.getLogManager().getLogger("DatasourceProfessors")
            .info("\nAdded professor with response: " + response.asString());
        return professor;
    }

    private Object options(final Professor professor) {
        return new JSONObject()
            .put("table", "professors")
            .put("values", new JsonPopulator(professor)
                .toMap());
    }

    @Override
    public Professor update(final Professor professor) throws ProfessorNotFoundException {
        final Professor original = get(professor.id());
        final JSONObject request = new JSONObject()
            .put("namespace", "professors")
            .put("options", updateOptions(original, professor))
            .put("type", "UPDATE");

        final Response response = execute(request);
        LogManager.getLogManager().getLogger("DatasourceProfessors")
            .info("\nAdded professor with response: " + response.asString());
        return get(professor.id());
    }

    private Object updateOptions(final Professor original, final Professor professor) {
        return new JSONObject()
            .put("table", "professors")
            .put("id", new JSONObject().put("id", original.id().toString()))
            .put("values", new UpdatePopulator(
                new JsonPopulator(original).toMap(), professor).toMap()
            );
    }

    @Override
    public void delete(final ObjectId id) {
        // We're not going to check if prof exists, we don't really care.
        final JSONObject request = new JSONObject()
            .put("namespace", "professors")
            .put("options", deleteOptions(id))
            .put("type", "DELETE");

        execute(request);
    }

    private Object deleteOptions(final ObjectId professorId) {
        return new JSONObject()
            .put("table", "professors")
            .put("id", new JSONObject().put("id", professorId.toString()));
    }

    private Response execute(final Object body) {
        return request.execute(body);
    }

    // TODO: This is just get us up-and-running.
    //  Make factory or something to encapsulates the common code.
    private static final class JsonPopulator implements Professor.ProfessorPopulator {

        private final Map<String, Object> json;
        private final Professor professor;

        JsonPopulator(final Professor professor) {
            json = new LinkedHashMap<>();
            this.professor = professor;
        }

        @Override
        public void firstName(final String value) {
            json.put("first_name", value);
        }

        @Override
        public void lastName(final String value) {
            json.put("last_name", value);
        }

        @Override
        public void id(final ObjectId id) {
            json.put("id", id.toString());
        }

        @Override
        public void departmentId(final ObjectId id) {
            // Temporary until we populate faculty
            json.put("department_id", Optional.ofNullable(id).map(ObjectId::toString).orElse(""));
        }

        @Override
        public void email(final String value) {
            json.put("email", value);
        }

        Map<String, Object> toMap() {
            professor.populate(this);
            return json;
        }
    }

    private static final class UpdatePopulator implements Professor.ProfessorPopulator {

        private final Map<String, Object> original;
        private final Professor professor;
        UpdatePopulator(final Map<String, Object> original, final Professor professor) {
            this.original = original;
            this.professor = professor;
        }
        @Override
        public void firstName(final String value) {
            set("first_name", value);
        }

        @Override
        public void lastName(final String value) {
            set("last_name", value);
        }

        @Override
        public void id(final ObjectId id) {

        }

        @Override
        public void departmentId(final ObjectId id) {
            set("department_id", id);
        }

        @Override
        public void email(final String value) {

        }

        Map<String, Object> toMap() {
            professor.populate(this);
            return original;
        }

        private void set(final String name, final Object value) {
            final Object oldValue = original.get(name);
            if (!Objects.toString(value, "").isBlank() && !Objects.equals(oldValue, value)) {
                original.put(name, value.toString());
            }
        }
    }
}

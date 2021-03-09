package robhopkins.wc.professors.request;

import org.json.JSONObject;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.ProfessorBuilder;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorException;

public final class UpdateProfessor {

    public static UpdateProfessor from(final ObjectId id, final String json) throws ProfessorException {
        final UpdateProfessor professor = Payload.newPayload(UpdateProfessor.class)
            .withJSON(new JSONObject(json))
            .withSchema("firstName:false", "lastName:false", "departmentId:false")
            .build();

        professor.id(id);
        return professor;
    }

    private final JSONObject json;
    private ObjectId id;

    UpdateProfessor(final JSONObject json) {
        this.json = json;
    }

    public ObjectId departmentId() {
        return ObjectId.from(value("departmentId"));
    }

    public Professor toProfessor() {
        return ProfessorBuilder.newBuilder()
            .withId(id)
            .withFirstName(value("firstName"))
            .withLastName(value("lastName"))
            .withDepartmentId(departmentId())
            .build();
    }

    private void id(final ObjectId id) {
        this.id = id;
    }

    private String value(final String field) {
        return json.has(field)
            ? json.getString(field)
            : "";
    }
}

package robhopkins.wc.professors.request;

import org.json.JSONObject;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.ProfessorBuilder;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorException;

public final class AddProfessor {

    public static AddProfessor from(final String json) throws ProfessorException {
        return Payload.newPayload(AddProfessor.class)
            .withJSON(new JSONObject(json))
            .withSchema("firstName", "lastName", "facultyId")
            .build();
    }

    private final JSONObject json;

    AddProfessor(final JSONObject json) {
        this.json = json;
    }

    public ObjectId facultyId() {
        return ObjectId.from(json.getString("facultyId"));
    }

    public Professor toProfessor() {
        return ProfessorBuilder.newBuilder()
            .withFirstName(json.getString("firstName"))
            .withLastName(json.getString("lastName"))
            .withFacultyId(facultyId())
            .build();
    }
}

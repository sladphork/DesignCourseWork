package robhopkins.wc.professors.request;

import org.json.JSONObject;
import robhopkins.wc.common.request.exception.RequestException;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.ProfessorBuilder;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorException;

public final class AddProfessor {

    public static AddProfessor from(final String json) throws ProfessorException {
        try {
            return create(json);
        } catch (RequestException e) {
            throw  new ProfessorException(e.getMessage()) {
                @Override
                public int status() {
                    return e.status();
                }
            };
        }
    }

    private static AddProfessor create(final String json) throws RequestException {
        return robhopkins.wc.common.request.Payload.newPayload(AddProfessor.class)
            .withJSON(new JSONObject(json))
            .withSchema("firstName", "lastName", "departmentId")
            .build();
    }

    private final JSONObject json;

    AddProfessor(final JSONObject json) {
        this.json = json;
    }

    public ObjectId departmentId() {
        return ObjectId.from(json.getString("departmentId"));
    }

    public Professor toProfessor() {
        return ProfessorBuilder.newBuilder()
            // TODO: Might not want to keep this here, but it's here for now.
            //  Not sure the best approach, but this should work.
            .withId(ObjectId.random())
            .withFirstName(json.getString("firstName"))
            .withLastName(json.getString("lastName"))
            .withDepartmentId(departmentId())
            .withEmail(generateEmail())
            .build();
    }

    private String generateEmail() {
        return String.format("%s.%s@wc.edu",
            json.getString("firstName"),
            json.getString("lastName")
        );
    }
}

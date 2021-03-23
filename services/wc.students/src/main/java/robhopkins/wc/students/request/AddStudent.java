package robhopkins.wc.students.request;

import org.json.JSONObject;
import robhopkins.wc.common.request.Payload;
import robhopkins.wc.common.request.exception.RequestException;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.StudentBuilder;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.StudentException;

public final class AddStudent {

    public static AddStudent from(final String json) throws StudentException {
        try {
            return create(json);
        } catch (RequestException e) {
            throw new StudentException(e.getMessage()) {
                @Override
                protected int status() {
                    return e.status();
                }
            };
        }
    }

    private static AddStudent create(final String json) throws RequestException {
        return Payload.newPayload(AddStudent.class)
            .withJSON(new JSONObject(json))
            .withSchema("firstName", "lastName", "facultyId")
            .build();
    }

    private final JSONObject json;

    AddStudent(final JSONObject json) {
        this.json = json;
    }

    public ObjectId facultyId() {
        return ObjectId.from(json.getString("facultyId"));
    }

    public Student toStudent() {
        return StudentBuilder.newBuilder()
            // TODO: Might not want to keep this here, but it's here for now.
            //  Not sure the best approach, but this should work.
            .withId(ObjectId.random())
            .withFirstName(json.getString("firstName"))
            .withLastName(json.getString("lastName"))
            .withFacultyId(facultyId())
            .withEmail(generateEmail())
            .build();
    }

    private String generateEmail() {
        // TODO: Change this as needed for students.
        return String.format("%s.%s@wc.edu",
            json.getString("firstName"),
            json.getString("lastName")
        );
    }
}

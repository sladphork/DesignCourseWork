package robhopkins.wc.students.request;

import org.json.JSONObject;
import robhopkins.wc.common.request.Payload;
import robhopkins.wc.common.request.exception.RequestException;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.StudentBuilder;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.StudentException;

public final class UpdateStudent {

    public static UpdateStudent from(final ObjectId id, final String json) throws StudentException {
        try {
            return create(id, json);
        } catch (RequestException e) {
            throw new StudentException(e.getMessage()) {
                @Override
                protected int status() {
                    return e.status();
                }
            };
        }
    }

    private static UpdateStudent create(final ObjectId id, final String json) throws RequestException {
        final UpdateStudent student = Payload.newPayload(UpdateStudent.class)
            .withJSON(new JSONObject(json))
            .withSchema("firstName:false", "lastName:false", "facultyId:false")
            .build();

        student.id(id);
        return student;
    }

    private final JSONObject json;
    private ObjectId id;

    UpdateStudent(final JSONObject json) {
        this.json = json;
    }

    public ObjectId facultyId() {
        return ObjectId.from(value("facultyId"));
    }

    public Student toStudent() {
        return StudentBuilder.newBuilder()
            .withId(id)
            .withFirstName(value("firstName"))
            .withLastName(value("lastName"))
            .withFacultyId(facultyId())
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

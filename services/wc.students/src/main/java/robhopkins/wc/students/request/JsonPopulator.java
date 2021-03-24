package robhopkins.wc.students.request;

import org.json.JSONObject;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.domain.ObjectId;

import java.util.Optional;

final class JsonPopulator implements Student.StudentPopulator {

    private final JSONObject json;
    private final Student student;

    JsonPopulator(final Student student) {
        json = new JSONObject();
        this.student = student;
    }

    @Override
    public void firstName(final String value) {
        json.put("firstName", value);
    }

    @Override
    public void lastName(final String value) {
        json.put("lastName", value);
    }

    @Override
    public void id(final ObjectId id) {
        json.put("id", id.toString());
    }

    @Override
    public void facultyId(final ObjectId id) {
        json.put("facultyId", Optional.ofNullable(id).map(ObjectId::toString).orElse(""));
    }

    @Override
    public void email(final String value) {
        json.put("email", value);
    }

    @Override
    public String toString() {
        student.populate(this);
        return json.toString(2);
    }
}

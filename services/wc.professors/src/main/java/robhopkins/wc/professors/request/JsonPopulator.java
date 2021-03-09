package robhopkins.wc.professors.request;

import org.json.JSONObject;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.domain.ObjectId;

import java.util.Optional;

final class JsonPopulator implements Professor.ProfessorPopulator {

    private final JSONObject json;
    private final Professor professor;

    JsonPopulator(final Professor professor) {
        json = new JSONObject();
        this.professor = professor;
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
    public void departmentId(final ObjectId id) {
        // Temporary until we populate faculty
        json.put("departmentId", Optional.ofNullable(id).map(ObjectId::toString).orElse(""));
    }

    @Override
    public void email(final String value) {
        json.put("email", value);
    }

    @Override
    public String toString() {
        professor.populate(this);
        return json.toString(2);
    }
}

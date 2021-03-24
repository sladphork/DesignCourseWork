package robhopkins.wc.students.db;

import org.json.JSONObject;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.domain.ObjectId;

final class TestStudent implements Student, Student.StudentPopulator {

    private final JSONObject source;
    private boolean equals;

    TestStudent(final JSONObject source) {
        this.source = source;
        equals = true;
    }

    @Override
    public ObjectId id() {
        return ObjectId.from(source.getString("ID"));
    }

    @Override
    public boolean equals(final Object o) {
        final Student other = (Student) o;
        other.populate(this);
        return equals;
    }

    @Override
    public void firstName(final String value) {
        equals &= source.get("FIRST_NAME").equals(value);
    }

    @Override
    public void lastName(final String value) {
        equals &= source.get("LAST_NAME").equals(value);
    }

    @Override
    public void id(final ObjectId id) {
        equals &= source.get("ID").equals(id.toString());
    }

    @Override
    public void facultyId(final ObjectId id) {
        equals &= source.get("FACULTY_ID").equals(id.toString());
    }

    @Override
    public void email(final String value) {
        equals &= source.get("EMAIL").equals(value);
    }

    @Override
    public void populate(final StudentPopulator populator) {
        populator.id(id());
        populator.facultyId(ObjectId.from(source.get("FACULTY_ID")));
        populator.firstName(source.getString("FIRST_NAME"));
        populator.lastName(source.getString("LAST_NAME"));
        populator.email(source.getString("EMAIL"));
    }
}

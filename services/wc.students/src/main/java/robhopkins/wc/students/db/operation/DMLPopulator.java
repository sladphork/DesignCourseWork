package robhopkins.wc.students.db.operation;

import robhopkins.wc.students.Student;
import robhopkins.wc.students.domain.ObjectId;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

final class DMLPopulator implements Student.StudentPopulator {

    static DMLPopulator forStudent(final Student student) {
        return forAdd(student);
    }

    static DMLPopulator forAdd(final Student student) {
        return new DMLPopulator(student, Collections.emptyMap(), other -> true);
    }

    static DMLPopulator forUpdate(final Student student, final Map<String, Predicate<Object>> setTests) {
        return new DMLPopulator(student, setTests, other -> false);
    }

    private final Student student;
    private final Map<String, Predicate<Object>> setTests;
    private final Map<String, Object> map;
    private final Predicate<Object> defaultPredicate;

    private DMLPopulator(final Student student, final Map<String, Predicate<Object>> setTests,
                         final Predicate<Object> defaultPredicate) {
        this.student = student;
        this.setTests = setTests;
        this.defaultPredicate = defaultPredicate;
        this.map = new LinkedHashMap<>();
    }

    Map<String, Object> toMap() {
        student.populate(this);
        return map;
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
        set("id", id);
    }

    @Override
    public void facultyId(final ObjectId id) {
        set("faculty_id", id);
    }

    @Override
    public void email(final String value) {
        set("email", value);
    }

    private void set(final String name, final Object value) {
        if (setTests.getOrDefault(name, defaultPredicate).test(value)) {
            map.put(name, String.valueOf(value));
        }
    }
}

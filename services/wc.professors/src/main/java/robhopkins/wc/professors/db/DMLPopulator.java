package robhopkins.wc.professors.db;

import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.domain.ObjectId;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

final class DMLPopulator implements Professor.ProfessorPopulator {

    static DMLPopulator forProfessor(final Professor professor) {
        return forAdd(professor);
    }

    static DMLPopulator forAdd(final Professor professor) {
        return new DMLPopulator(professor, Collections.emptyMap(), other -> true);
    }

    static DMLPopulator forUpdate(final Professor professor, final Map<String, Predicate<Object>> setTests) {
        return new DMLPopulator(professor, setTests, other -> false);
    }

    private final Professor professor;
    private final Map<String, Predicate<Object>> setTests;
    private final Map<String, Object> map;
    private final Predicate<Object> defaultPredicate;

    private DMLPopulator(final Professor professor, final Map<String, Predicate<Object>> setTests,
                         final Predicate<Object> defaultPredicate) {
        this.professor = professor;
        this.setTests = setTests;
        this.defaultPredicate = defaultPredicate;
        this.map = new LinkedHashMap<>();
    }

    Map<String, Object> toMap() {
        professor.populate(this);
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
    public void departmentId(final ObjectId id) {
        set("department_id", id);
    }

    @Override
    public void email(final String value) {
        set("email", value);
    }

    private void set(final String name, final Object value) {
        if (setTests.getOrDefault(name, defaultPredicate).test(value)) {
            map.put(name, value.toString());
        }
    }
}

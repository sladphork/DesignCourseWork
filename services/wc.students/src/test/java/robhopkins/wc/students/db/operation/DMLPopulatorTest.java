package robhopkins.wc.students.db.operation;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.domain.ObjectId;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;

final class DMLPopulatorTest {

    @Test
    void toMapForAddShouldSetAllFields() {
        final DMLPopulator populator = DMLPopulator.forAdd(studentForAdd());

        final Map<String, Object> map = populator.toMap();
        assertThat(map, containsExpectedValuesForAdd());
    }

    @Test
    void toMapForUpdateShouldOnlySetSomeFields() {
        final DMLPopulator populator = DMLPopulator.forUpdate(studentForUpdate(), updatePredicates());

        final Map<String, Object> map = populator.toMap();
        assertThat(map, containsExpectedValuesForUpdate());
    }

    private Map<String, Predicate<Object>> updatePredicates() {
        return Map.of(
            "first_name", other -> true,
            "last_name", other -> true,
            "faculty_id", other -> false // The faculty id has not changed for this test.
        );
    }

    private Matcher<Map<String, Object>> containsExpectedValuesForAdd() {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> map = (Map<String, Object>)o;
                return ObjectId.from("12345").equals(from("id", map))
                    && ObjectId.from("98765").equals(from("faculty_id", map))
                    && "AddTest".equals(map.get("first_name"))
                    && "Student".equals(map.get("last_name"))
                    && "AddTest.Student@wc.edu".equals(map.get("email"));
            }

            private ObjectId from(final String field, final Map<String, Object> map) {
                return ObjectId.from(map.get(field));
            }

            @Override
            public void describeTo(final Description description) {

            }
        };
    }

    private Matcher<Map<String, Object>> containsExpectedValuesForUpdate() {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> map = (Map<String, Object>)o;
                return Objects.isNull(map.get("id"))
                    && Objects.isNull(map.get("email"))
                    && Objects.isNull(map.get("faculty_id"))
                    && "UpdateTest".equals(map.get("first_name"))
                    && "NewName".equals(map.get("last_name"));
            }

            private ObjectId from(final String field, final Map<String, Object> map) {
                return (ObjectId) map.get(field);
            }

            @Override
            public void describeTo(final Description description) {

            }
        };
    }

    private Student studentForAdd() {
        return new Student() {
            @Override
            public ObjectId id() {
                return ObjectId.from("12345");
            }

            @Override
            public void populate(final StudentPopulator populator) {
                populator.id(id());
                populator.firstName("AddTest");
                populator.lastName("Student");
                populator.facultyId(ObjectId.from("98765"));
                populator.email("AddTest.Student@wc.edu");
            }
        };
    }

    private Student studentForUpdate() {
        return new Student() {
            @Override
            public ObjectId id() {
                return ObjectId.from("12345");
            }

            @Override
            public void populate(final StudentPopulator populator) {
                populator.id(id());
                populator.firstName("UpdateTest");
                populator.lastName("NewName");
                populator.facultyId(ObjectId.from("45678"));
                populator.email("AddTest.Student@wc.edu");
            }
        };
    }
}

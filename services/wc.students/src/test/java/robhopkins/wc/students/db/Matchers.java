package robhopkins.wc.students.db;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import robhopkins.wc.students.Student;

import java.util.function.Predicate;

final class Matchers {

    static Matcher<Student> student(final JSONObject dsJson) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                return new TestStudent(dsJson).equals(o);
            }

            @Override
            public void describeTo(final Description description) {

            }
        };
    }

    static Matcher<DatasourceServiceMock> hasPayload(final String namespace, final String table) {
        return hasPayload(namespace, table, options -> true);
    }

    static Matcher<DatasourceServiceMock> hasPayload(final String namespace, final String table,
                                                     final Predicate<JSONObject> optionsTest) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final JSONObject payload = ((DatasourceServiceMock)o).payload();
                final JSONObject options = payload.getJSONObject("options");
                return payload.optString("namespace", "").equals(namespace)
                    && options.optString("table", "").equals(table)
                    && optionsTest.test(options);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}

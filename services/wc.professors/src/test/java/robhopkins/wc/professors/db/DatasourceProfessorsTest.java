package robhopkins.wc.professors.db;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.request.UpdateProfessor;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class DatasourceProfessorsTest {

    @Test
    void updateShouldSendCorrectValues() throws Exception {
        final UpdateActionSpy action = new UpdateActionSpy(
            professorResponse(),
            updateResponse(),
            professorResponse()
        );
        final DatasourceProfessors professors = new DatasourceProfessors(action);
        final Professor toUpdate = professorToUpdate("Updated", "LastName", "45678");

        final Professor updated = professors.update(toUpdate);
        assertThat(updated, notNullValue());
        assertThat(action.payload(), isExpectedUpdate());

    }

    private Matcher<JSONObject> isExpectedUpdate() {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final JSONObject json = (JSONObject) o;
                final JSONObject options = json.getJSONObject("options");
                return "professors".equals(json.getString("namespace"))
                    && "UPDATE".equals(json.getString("type"))
                    && "professors".equals(options.getString("table"))
                    && "12345".equals(String.valueOf(options.getJSONObject("id").get("id")))
                    && matchesValues(options.getJSONObject("values"));
            }

            private boolean matchesValues(final JSONObject values) {
                return "Updated".equals(values.getString("first_name"))
                    && "LastName".equals(values.getString("last_name"))
                    && "45678".equals(values.getString("department_id"))
                    && !values.has("id")
                    && !values.has("email");
            }

            @Override
            public void describeTo(final Description description) {

            }
        };
    }
    private Professor professorToUpdate(final String firstName,
                                        final String lastName,
                                        final String departmentId)
        throws Exception {

        final String json = new JSONObject()
            .put("firstName", firstName)
            .put("lastName", lastName)
            .put("departmentId", departmentId)
            .toString();
        return UpdateProfessor.from(ObjectId.from("12345"), json).toProfessor();
    }

    private DatasourceResponse professorResponse() {
        final DatasourceResponse response = mock(DatasourceResponse.class);
        when(response.body()).thenReturn(toJSON(
            Map.of(
                "ID", "12345",
                "FIRST_NAME", "First",
                "LAST_NAME", "Last",
                "DEPARTMENT_ID", "98765",
                "EMAIL", "first.last@wc.edu"
            )
        ));
        return response;
    }

    private DatasourceResponse updateResponse() {
        final DatasourceResponse response = mock(DatasourceResponse.class);
        when(response.body()).thenReturn("{\"result\": \"Success\"}");
        return response;
    }

    private String toJSON(final Map<String, Object> values) {
        return "[" + (new JSONObject(values).toString(2)) + "]";
    }
}

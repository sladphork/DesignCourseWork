package robhopkins.wc.professors.db;

import io.restassured.response.Response;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
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
        final DatasourceRequestSpy request = new DatasourceRequestSpy(
            professorResponse(),
            updateResponse(),
            professorResponse()
        );
        final DatasourceProfessors professors = new DatasourceProfessors(request);
        final Professor toUpdate = professorToUpdate("Updated", "LastName", "45678");

        final Professor updated = professors.update(toUpdate);
        assertThat(updated, notNullValue());
        final JSONObject updatePayload = request.updatePayload();
        assertThat(updatePayload, isExpectedUpdate());

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
                    && "12345".equals(options.getJSONObject("id").getString("id"))
                    && matchesValues(options.getJSONObject("values"));
            }

            private boolean matchesValues(final JSONObject values) {
                return "Updated".equals(values.getString("first_name"))
                    && "LastName".equals(values.getString("last_name"))
                    && "45678".equals(values.getString("department_id"))
                    // TODO: Right now we set these, but going forward can probably remove them.
                    && "12345".equals(values.getString("id"))
                    && "first.last@wc.edu".equals(values.getString("email"));
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

    private Response professorResponse() {
        final Response response = mock(Response.class);
        when(response.asString()).thenReturn(toJSON(
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

    private Response updateResponse() {
        final Response response = mock(Response.class);
        when(response.asString()).thenReturn("{\"result\": \"Success\"}");
        return response;
    }

    private String toJSON(final Map<String, Object> values) {
        return "[" + (new JSONObject(values).toString(2)) + "]";
    }


    private static final class DatasourceRequestSpy implements DatasourceRequest {

        private final Response[] responses;

        private JSONObject updatePayload;
        private int current;
        DatasourceRequestSpy(final Response... responses) {
            this.responses = responses;
        }

        @Override
        public Response execute(final Object body) {
            if (updatePayload(body)) {
                this.updatePayload = (JSONObject) body;
            }
            return responses[current++];
        }

        private boolean updatePayload(final Object body) {
            final JSONObject payload = (JSONObject) body;
            return "UPDATE".equals(payload.getString("type"));
        }

        JSONObject updatePayload() {
            return updatePayload;
        }
    }
}

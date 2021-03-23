package robhopkins.wc.common.request;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import robhopkins.wc.common.request.exception.InvalidBodyException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;

final class PayloadTest {

    @Test
    void buildWithoutSchemaAndJsonShouldSucceed() throws Exception {
        final TestPayload payload = Payload.newPayload(TestPayload.class)
            .build();

        assertThat(payload, notNullValue());
        assertThat(payload.isEmpty(), is(Boolean.TRUE));
    }

    @Test
    void buildWithAMissingField() throws Exception {
        final String json = json("field2");
        try {
            Payload.newPayload(TestPayload.class)
                .withJSON(json)
                .withSchema("field1", "field2")
                .build();
        } catch (InvalidBodyException e) {
            assertThat(e.status(), is(400));
            assertThat(e.getMessage(), containsString("field1"));
        }
    }

    @Test
    void buildWithInvalidSchema() throws Exception {
        final String json = json("field2");
        try {
            Payload.newPayload(TestPayload.class)
                .withJSON(json)
                .withSchema("field1:", "field2")
                .build();
        } catch (InvalidBodyException e) {
            assertThat(e.status(), is(400));
            assertThat(e.getMessage(), containsString("field1"));
        }
    }

    @Test
    void buildWithAMissingNonRequiredField() throws Exception {
        final String json = json("field2");
        final TestPayload payload = Payload.newPayload(TestPayload.class)
            .withJSON(json)
            .withSchema("field1:false", "field2")
            .build();

        assertThat(payload.matches("field2", "Value for field2"), is(Boolean.TRUE));
    }

    @Test
    void buildWithSchemaAndJsonShouldSucceed() throws Exception {
        final TestPayload payload = Payload.newPayload(TestPayload.class)
            .withJSON(json("field1", "field2"))
            .withSchema("field1", "field2")
            .build();

        assertThat(payload.matches("field1", "Value for field1"), is(Boolean.TRUE));
        assertThat(payload.matches("field2", "Value for field2"), is(Boolean.TRUE));
    }

    private String json(final String... fields) {
        final JSONObject json = new JSONObject();
          for (String field: fields) {
              json.put(field, "Value for " + field);
          }
          return json.toString(2);
    }

    static final class TestPayload {
        private final JSONObject json;
        TestPayload(final JSONObject json) {
            this.json = json;
        }

        boolean isEmpty() {
            return json.isEmpty();
        }

        boolean matches(final String field, final Object expectedValue) {
            return expectedValue.equals(json.opt(field));
        }
    }
}

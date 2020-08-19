package robhopkins.util;

import io.restassured.response.Response;
import org.assertj.core.api.Condition;
import org.json.JSONObject;

import java.util.Objects;
import java.util.function.Predicate;

public final class Conditions {
    private Conditions() {

    }

    private static final String ERROR_TYPE = "application/vnd.wc.error.v1+json";

    public static Condition<? super Response> okResponse() {
        return okResponse("");
    }

    public static Condition<? super Response> okResponse(final String contentType) {
        return new Condition<>(
            ResponsePredicate.newPredicate().withContentType(contentType),
            "Response is not OK"
        );
    }

    public static Condition<? super Response> unauthorized() {
        return new Condition<>(
            ResponsePredicate.newErrorPredicate(401),
            "Response is not Unauthorized."
        );
    }

    public static Condition<? super String> validString() {
        return new Condition<>(
            value -> Objects.toString(value, "").length() > 0,
            "String is not valid."
        );
    }

    public static Condition<? super JSONObject> fieldWithValue(final String field, final Object value) {
        return new Condition<>(
            json -> jsonHas(field, value, json),
            String.format("JSON does not match: %s with %s", field, value)
        );
    }

    private static boolean jsonHas(final String field, final Object value, final JSONObject json) {
        return json.has(field) && value.equals(json.get(field));
    }

    private static final class ResponsePredicate implements Predicate<Response> {
        static ResponsePredicate newPredicate() {
            return new ResponsePredicate();
        }

        static ResponsePredicate newErrorPredicate(final int status) {
            return new ResponsePredicate()
                .withStatus(status)
                .withContentType(ERROR_TYPE);
        }

        private String contentType = "application/vnd.wc.v1+json";
        private int status = 200;

        private ResponsePredicate() {
        }

        @Override
        public boolean test(final Response response) {
            return response.getStatusCode() == status
                && contentType(response).equals(contentType);
        }

        ResponsePredicate withContentType(final String contentType) {
            this.contentType = contentType;
            return this;
        }

        ResponsePredicate withStatus(final int status) {
            this.status = status;
            return this;
        }

        private String contentType(final Response response) {
            return Objects.toString(response.getContentType(), "");
        }
    }
}

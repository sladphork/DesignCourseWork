package robhopkins.wc.professors.request;

import org.json.JSONObject;
import robhopkins.wc.professors.exception.InvalidBodyException;
import robhopkins.wc.professors.exception.ProfessorException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

final class Payload<T> {
    static <T> Payload<T> newPayload(final Class<T> type) {
        return new Payload<>(type);
    }

    private String[] schema;
    private JSONObject json;

    private final Class<T> type;

    private Payload(final Class<T> type) {
        this.type = type;
    }

    Payload<T> withSchema(final String... schema) {
        this.schema = schema;
        return this;
    }

    Payload<T> withJSON(final JSONObject json) {
        this.json = json;
        return this;
    }

    <T> T build() throws ProfessorException {
        validate();
        try {
            @SuppressWarnings("unchecked") final Constructor<T> ctor = (Constructor<T>) type.getDeclaredConstructor(JSONObject.class);
            return ctor.newInstance(json);
        } catch (Exception e) {
            throw new ServerException(e);
        }
    }

    private void validate() throws InvalidBodyException {
        final String[] invalidFields = invalidFields(json);
        if (invalidFields.length > 0) {
            throw new InvalidBodyException(type.getSimpleName(), invalidFields);
        }
    }

    private String[] invalidFields(final JSONObject json) {
        return Arrays.stream(schema)
            .filter(field -> isRequired(field) && (!json.has(field) || json.getString(field).isBlank()))
            .collect(Collectors.toSet())
            .toArray(new String[]{});
    }

    private boolean isRequired(final String field) {
        final String[] parts = field.split(":");
        return parts.length == 1 || Boolean.parseBoolean(parts[1]);
    }

    private static final class ServerException extends ProfessorException {
        ServerException(final Throwable cause) {
            super(cause.getMessage());
        }

        @Override
        protected int status() {
            return 500;
        }
    }
}

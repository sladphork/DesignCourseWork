package robhopkins.wc.iam.request;

import org.json.JSONObject;
import robhopkins.wc.iam.request.exception.IAMException;
import robhopkins.wc.iam.request.exception.InvalidBodyException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class Payload<T> {
    public static <T> Payload<T> newPayload(final Class<T> type) {
        return new Payload<>(type);
    }

    private String[] schema;
    private JSONObject json;

    private final Class<T> type;

    private Payload(final Class<T> type) {
        this.type = type;
    }

    public Payload<T> withSchema(final String... schema) {
        this.schema = schema;
        return this;
    }

    public Payload<T> withJSON(final JSONObject json) {
        this.json = json;
        return this;
    }

    public T build() throws IAMException {
        validate();
        try {
            final Constructor<T> ctor = type.getDeclaredConstructor(JSONObject.class);
            ctor.setAccessible(true);
            return ctor.newInstance(json);
        } catch (Exception e) {
            throw new ServerException(e);
        }
    }

    private void validate() throws IAMException {
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

    private static final class ServerException extends IAMException {
        ServerException(final Throwable cause) {
            super(cause.getMessage());
        }

        @Override
        public int status() {
            return 500;
        }
    }
}

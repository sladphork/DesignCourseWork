package robhopkins.wc.common.request;

import org.json.JSONObject;
import robhopkins.wc.common.request.exception.InvalidBodyException;
import robhopkins.wc.common.request.exception.RequestException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is a class that can be used to read the JSON payload of a request.
 * Typically, it will be used during POST and PUT requests but not necessarily.
 *
 * Example usage:
 * <code>
 *     final ExampleObject object = Payload.newPayload(ExampleObject.class)
 *             .withSchema("field1", "field2:false")
 *             .withJson([json value from request])
 *             .build();
 *
 *    final String value1 = object.getField1();
 *    final String value2 = object.getField2();
 *
 *    class ExampleObject {
 *        // Note that this constructor is package-private and takes a JSONObject.
 *        //  This is a requirement.
 *        ExampleObject(final JSONObject json) {
 *            // set fields, etc. as desired using the json provided
 *        }
 *
 *        String getField1() {
 *            // Since this is a required field
 *            return json.getString("field1");
 *        }
 *
 *        String getField2() {
 *            // This is an optional field
 *            return json.optString("field2");
 *        }
 *    }
 * </code>
 *
 * @param <T> the type of value that will be returned by
 *           the {@link #build()} method.
 */
public final class Payload<T> {

    /**
     * Creates a new {@code Payload} instance that can be used to create
     * a new value of the specified type.
     *
     * @param type the type of the value that will be returned by
     *             the {@link #build()} method
     * @param <T> the type of the value.
     *
     * @return a new {@code Payload} instance that can build values
     *         of type T
     */
    public static <T> Payload<T> newPayload(final Class<T> type) {
        return new Payload<>(type);
    }

    private String[] schema = new String[]{};
    private JSONObject json;

    private final Class<T> type;

    private Payload(final Class<T> type) {
        this.type = type;
    }

    /**
     * Defines which values in the JSON are required.
     * If no schema is defined then none of the values are required.
     *
     * Examples:
     * <code>
     *     // All required
     *     "field1", "field2", ...
     *     // Some required
     *     "field1", "field2:false", ...
     * </code>
     * <b>Note</b>: That a field without "true" or "false" is required and if it has the ':'
     *              but no value, it defaults to "true".
     *
     * @param schema a list of string values representing fields
     *               that are required or optional.
     *
     * @return the current payload.
     */
    public Payload<T> withSchema(final String... schema) {
        this.schema = schema;
        return this;
    }

    /**
     * Sets the JSON that will be parsed and validated.
     *
     * @param json a {@link JSONObject} with the desired values.
     *
     * @return the current payload.
     */
    public Payload<T> withJSON(final JSONObject json) {
        this.json = json;
        return this;
    }

    /**
     * Sets the JSON that will be parsed and validated.
     * This is a convenience method, as this is the same as calling
     * withJSON(new JSONObject(json))}
     *
     * @param json a string valure representing the JSON with the desired values.
     *
     * @return the current payload.
     */
    public Payload<T> withJSON(final String json) {
        return withJSON(new JSONObject(json));
    }

    /**
     * Validates the supplied JSON against the schema to ensure it has the
     * required fields and then it builds a new value of type <T>.
     *
     * @return a new value of type T with the validated JSON.
     *
     * @throws RequestException for any issue with creating the value.
     */
    public T build() throws RequestException {
        validate();
        try {
            final Constructor<T> ctor = type.getDeclaredConstructor(JSONObject.class);
            ctor.setAccessible(true);
            return ctor.newInstance(json());
        } catch (Exception e) {
            throw new ServerException(e);
        }
    }

    private JSONObject json() {
        return Optional.ofNullable(json)
            .orElse(new JSONObject());
    }

    private void validate() throws InvalidBodyException {
        final String[] invalidFields = invalidFields(json());
        if (invalidFields.length > 0) {
            throw new InvalidBodyException(type.getSimpleName(), invalidFields);
        }
    }

    private String[] invalidFields(final JSONObject json) {
        return Arrays.stream(schema)
            .filter(field -> isRequired(field) && json.optString(field).isBlank())
            .collect(Collectors.toSet())
            .toArray(new String[]{});
    }

    private boolean isRequired(final String field) {
        final String[] parts = field.split(":");
        return parts.length == 1 || Boolean.parseBoolean(parts[1]);
    }

    private static final class ServerException extends RequestException {
        ServerException(final Throwable cause) {
            super(cause.getMessage());
        }

        @Override
        public int status() {
            return 500;
        }
    }
}

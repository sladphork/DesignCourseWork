package robhopkins.wc.iam.rest.request;

import org.takes.Request;
import robhopkins.wc.iam.rest.exception.BadRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * This is an echo class back to the FormData value used by the browser.
 * It represents form data submitted with the following content types:
 * <ul>
 * <li>multipart/form-data; boundary=----WebKitFormBoundaryvImlDdYuvAxTiFf6</li>
 * <li>application/x-www-form-urlencoded</li>
 * </ul>
 */
public abstract class FormData {

    private static final Pattern MULTIPART = Pattern.compile("(multipart/form-data(; boundary=.*)?)");
    private static final Pattern URL_ENCODED = Pattern.compile("(application/x-www-form-urlencoded).*");//(; boundary=.*)?)");

    private static final Map<Pattern, BiFunction<Request, String[], FormData>> BUILDERS = Map.of(
        MULTIPART, (req, schema) -> new MultipartForm(req, schema),
        URL_ENCODED, (req, schema) -> new UrlEncodedForm(req, schema)
    );

    /**
     * Creates a new {@code FormData} using the specified data and schema.
     *
     * @param request the {@link Request} with the form data.
     * @param schema  the collection of fields in the form (This may change and become more of an object).
     * @return a {@code FormData} instance.
     * @throws IOException for any issue with reading the request.
     */
    public static FormData newData(final Request request, final String... schema) throws IOException {
        final String contentType = ContentType.from(request).toString();
        return BUILDERS.entrySet().stream()
            .filter(builder -> builder.getKey().matcher(contentType).matches())
            .findFirst()
            .map(builder -> builder.getValue().apply(request, schema))
            .orElse(new UnknownType(contentType));
    }

    final Request request;
    final Collection<String> fields;

    FormData(final Request request, final String... schema) {
        this.request = request;
        this.fields = Arrays.asList(schema);
    }

    public Map<String, String> toMap() throws BadRequest {
        return wrap(
            parse()
        );
    }

    abstract Map<String, String> parse();

    private Map<String, String> wrap(final Map<String, String> values) throws BadRequest {
        if (values.size() != fields.size()) {
            throw new BadRequest("All fields were not sent!");
        }
        return Collections.unmodifiableMap(values);
    }


    private static final class UnknownType extends FormData {

        private final String contentType;
        UnknownType(String contentType) {
            super(null);
            this.contentType = contentType;
        }

        @Override
        public Map<String, String> toMap() throws BadRequest {
            throw new BadRequest(String.format("Unknown Content-Type; '%s'", contentType));
        }

        @Override
        Map<String, String> parse() {
            return null;
        }
    }
}

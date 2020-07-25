package robhopkins.wc.iam.rest.request;

import org.takes.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class MultipartForm extends FormData {

    private static final Pattern FIELD_NAME = Pattern.compile("Content-Disposition: form-data; name=\"(.*)\"");
    private static final int NAME_INDEX = 1;

    MultipartForm(final Request request, final String... schema) {
        super(request, schema);
    }

    @Override
    Map<String, String> parse() {
        final Map<String, String> values = new LinkedHashMap<>();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(request.body()))) {
            String line;
            FormValue value = new FormValue(fields, boundaryTest());

            while ((line = reader.readLine()) != null) {
                if (doAdd(value)) {
                    values.put(value.name, value.value);
                }

                value = value.read(line);

                if (doFinish(values)) {
                    return values;
                }
            }
        } catch (IOException e) {
            return Collections.emptyMap();
        }
        return values;
    }

    private boolean doAdd(final FormValue value) {
        return null != value.name && null != value.value;
    }

    private boolean doFinish(final Map<String, String> values) {
        return values.size() == fields.size();
    }

    private Predicate<String> boundaryTest() {
        // TODO: Get the boundary from the header, for now
        return line -> line.startsWith("---");
    }

    private static final class FormValue {
        private String value;
        private String name;

        private final Collection<String> fields;
        private final Predicate<String> boundaryTest;

        FormValue(final Collection<String> fields, final Predicate<String> boundaryTest) {
            this.fields = fields;
            this.boundaryTest = boundaryTest;
        }

        FormValue read(final String line) {
            final Matcher matcher = FIELD_NAME.matcher(line);
            if (matcher.matches()) {
                final String name = matcher.group(NAME_INDEX);

                return doCreateNew(name)
                    ? createNew(name)
                    : this;
            }

            if (doSetValue(line)) {
                this.value = line;
            }
            return this;
        }

        private boolean doCreateNew(final String name) {
            return this.fields.contains(name);
        }

        private FormValue createNew(final String name) {
            final FormValue value = new FormValue(fields, boundaryTest);
            value.name = name;
            return value;
        }

        private boolean doSetValue(final String line) {
            return null != this.name
                && !line.isEmpty()
                && isNotBoundary(line);
        }

        private boolean isNotBoundary(final String line) {
            return boundaryTest.negate().test(line);
        }

    }
}

package robhopkins.wc.iam.rest.request;

import org.takes.Request;
import org.takes.rq.RqForm;
import org.takes.rq.form.RqFormSmart;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final class UrlEncodedForm extends FormData {

    UrlEncodedForm(final Request request, final String... schema) {
        super(request, schema);
    }

    @Override
    Map<String, String> parse() {
        final RqFormSmart form = new RqFormSmart(request);
        final Set<String> names = names(form);

        return fields.stream()
            .filter(names::contains)
            .collect(Collectors.toMap(
                Function.identity(),
                field -> get(field, form)
            ));
    }

    private String get(final String name, final RqFormSmart form) {
        try {
            return form.single(name, "");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Set<String> names(final RqForm form) {
        try {
            return StreamSupport.stream(form.names().spliterator(), false)
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

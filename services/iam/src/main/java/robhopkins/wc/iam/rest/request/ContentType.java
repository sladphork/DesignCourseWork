package robhopkins.wc.iam.rest.request;

import org.takes.Request;

import java.io.IOException;
import java.util.stream.StreamSupport;

final class ContentType {
    static ContentType from(final Request request) throws IOException {
        final String contentType = StreamSupport.stream(request.head().spliterator(), false)
            .filter(head -> head.toLowerCase().startsWith("content-type"))
            .findFirst()
            .map(keyValue -> keyValue.split(":")[1].trim())
            .orElse("");

        return new ContentType(contentType);
    }

    private final String value;

    private ContentType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentType that = (ContentType) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}

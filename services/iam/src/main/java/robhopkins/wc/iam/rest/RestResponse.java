package robhopkins.wc.iam.rest;

import org.takes.Response;
import org.takes.rs.RsText;
import org.takes.rs.RsWithStatus;
import org.takes.rs.RsWithType;

import java.io.IOException;
import java.io.InputStream;

final class RestResponse implements Response {

    private final Response target;

    RestResponse(final int status, final String contentType, final Object response) {
        this(new RsWithStatus(
                new RsWithType(
                    new RsText(String.valueOf(response)),
                    contentType
                ),
                status
            )
        );
    }

    private RestResponse(final Response target) {
        this.target = target;
    }

    @Override
    public InputStream body() throws IOException {
        return target.body();
    }

    @Override
    public Iterable<String> head() throws IOException {
        return target.head();
    }
}
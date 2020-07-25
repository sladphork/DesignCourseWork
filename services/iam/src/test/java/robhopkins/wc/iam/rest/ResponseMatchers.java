package robhopkins.wc.iam.rest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.takes.Response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

final class ResponseMatchers {

    static Matcher<Response> isEmptyResponse() {
        return new ContentMatcher("HTTP/1.1 200 OK");
    }

    static Matcher<Response> isOkResponse() {
        return new ContentMatcher("HTTP/1.1 200 OK", "Content-Type: application/vnd.wc.iam.v1+json");
    }

    static Matcher<Response> isBadResponse() {
        return new ContentMatcher("HTTP/1.1 400 Bad Request", "Content-Type: application/vnd.wc.iam.error.v1+json");
    }

    static Matcher<Response> isUnauthorizedResponse() {
        return new ContentMatcher("HTTP/1.1 401 Unauthorized", "Content-Type: application/vnd.wc.iam.error.v1+json");
    }

    private static final class ContentMatcher extends BaseMatcher<Response> {

        private final List<String> patterns;
        ContentMatcher(final String... patterns) {
            this.patterns = Arrays.asList(
                patterns);
        }
        @Override
        public boolean matches(final Object o) {
            final Response response = (Response)o;
            try {
                return
                    StreamSupport.stream(response.head().spliterator(), false)
                        .filter(patterns::contains)
                        .count() == patterns.size();
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void describeTo(final Description description) {
            // TODO
        }
    }
}

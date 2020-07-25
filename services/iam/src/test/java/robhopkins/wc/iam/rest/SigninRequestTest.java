package robhopkins.wc.iam.rest;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;
import org.takes.Request;
import org.takes.Response;
import robhopkins.wc.iam.signin.Credentials;
import robhopkins.wc.iam.signin.SigninSheet;
import robhopkins.wc.iam.signin.Token;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.exception.AuthenticationException;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static robhopkins.wc.iam.rest.ResponseMatchers.isBadResponse;
import static robhopkins.wc.iam.rest.ResponseMatchers.isOkResponse;

final class SigninRequestTest {

    @Test
    void requestShouldHandleNoContentType() throws Exception {
        final SigninRequest handler = new SigninRequest(sheet());

        final Request request = RequestBuilder.newBuilder()
            .build();
        final Response response = handler.act(request);
        assertThat(response, notNullValue());
        assertThat(response, isBadResponse());
    }

    @Test
    void requestShouldHandleIncorrectContentType() throws Exception {
        final SigninRequest handler = new SigninRequest(sheet());

        final Request request = RequestBuilder.newBuilder()
            .withHeader("Content-Type", "application/json")
            .build();
        final Response response = handler.act(request);
        assertThat(response, notNullValue());
        assertThat(response, isBadResponse());
    }

    @Test
    void requestShouldHandleUsernameNotDefined() throws Exception {
        testMissing(
            "application/x-www-form-urlencoded",
            urlEncodedPayload("secret")
        );
        testMissing(
            "multipart/form-data; boundary=----WebKitFormBoundaryJ9YNbXWCLswuPDaV",
            formData("secret")
        );
    }

    @Test
    void requestShouldHandlePasswordNotDefined() throws Exception {
        testMissing(
            "application/x-www-form-urlencoded",
            urlEncodedPayload("username")
        );
        testMissing(
            "multipart/form-data; boundary=----WebKitFormBoundaryJ9YNbXWCLswuPDaV",
            formData("username")
        );
    }

    private void testMissing(final String contentType, final String body) throws Exception {
        final SigninRequest handler = new SigninRequest(sheet());

        final Request request = RequestBuilder.newBuilder()
            .withHeader("Content-Type", contentType)
            .withBody(body)
            .build();

        final Response response = handler.act(request);
        assertThat(contentType, response, notNullValue());
        assertThat(contentType, response, isBadResponse());
    }

    @Test
    void requestShouldReturnUrlEncodedForm() throws Exception {
        testSuccess(
            "application/x-www-form-urlencoded",
            urlEncodedPayload("username", "secret")
        );
    }

    @Test
    void requestShouldHandleMultiPartForm() throws Exception {
        testSuccess(
            "multipart/form-data; boundary=----WebKitFormBoundaryJ9YNbXWCLswuPDaV",
            formData("username", "secret")
        );
    }

    private void testSuccess(final String contentType, final String body) throws Exception {
        final SigninRequest handler = new SigninRequest(sheet());

        final Request request = RequestBuilder.newBuilder()
            .withHeader("Content-Type", contentType)
            .withBody(body)
            .build();

        final Response response = handler.act(request);
        assertThat(contentType, response, notNullValue());
        assertThat(contentType, response, isOkResponse());

        final JSONObject json = new JSONObject(new JSONTokener(response.body()));
        assertThat(contentType, json.has("token"), is(Boolean.TRUE));
    }

    private String urlEncodedPayload(final String... fields) {
        return Arrays.stream(fields)
            .map(field -> String.join("=", field, field))
            .collect(Collectors.joining("&"));
    }

    private String formData(final String... fields) {
        final String boundary = "------WebKitFormBoundaryJ9YNbXWCLswuPDaV\n";
        final String field = "Content-Disposition: form-data; name=\"%s\"\n" +
            "\n" +
            "%s\n";

        return Arrays.stream(fields)
            .map(value -> String.format(field, value, value))
            .collect(Collectors.joining(boundary, boundary, boundary));
    }

    private SigninSheet sheet() {
        return new SigninSheet() {
            @Override
            public Token signin(final Credentials credentials) throws AuthenticationException {
                return new Token() {
                    @Override
                    public String toJson() {
                        return "";
                    }

                    @Override
                    public UserId userId() {
                        return UserId.random();
                    }
                };
            }

            @Override
            public void signout(final Token token) {

            }
        };
    }
}

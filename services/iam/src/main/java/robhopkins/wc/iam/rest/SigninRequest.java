package robhopkins.wc.iam.rest;

import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import robhopkins.wc.iam.rest.exception.BadRequest;
import robhopkins.wc.iam.rest.exception.IAMException;
import robhopkins.wc.iam.rest.request.FormData;
import robhopkins.wc.iam.signin.Credentials;
import robhopkins.wc.iam.signin.SigninSheet;
import robhopkins.wc.iam.signin.Token;
import robhopkins.wc.iam.user.exception.AuthenticationException;

import java.io.IOException;
import java.util.Map;

final class SigninRequest implements Take {

    private final SigninSheet sheet;

    SigninRequest(final SigninSheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Response act(final Request req) throws IOException {

        System.out.println("Signin Request");

        try {
            return SigninResponseBuilder.newBuilder()
                .withToken(signin(req))
                .build();
        } catch (IAMException error) {
            return error.toResponse();
        }
    }

    private Token signin(final Request request) throws AuthenticationException, BadRequest, IOException {
        return sheet.signin(
            credentials(request)
        );
    }

    private Credentials credentials(final Request req) throws BadRequest, IOException {
        final FormData data = FormData.newData(req, "username", "secret");
        return new DefaultCredentials(data.toMap());
    }

    private static final class DefaultCredentials implements Credentials {

        private final Map<String, String> values;

        DefaultCredentials(final Map<String, String> data) {
            this.values = data;
        }

        @Override
        public String username() {
            return values.get("username");
        }

        @Override
        public byte[] secret() {
            return values.get("secret").getBytes();
        }
    }
}

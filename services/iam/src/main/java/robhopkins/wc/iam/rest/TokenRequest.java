package robhopkins.wc.iam.rest;

import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import robhopkins.wc.iam.signin.SigninSheet;

import java.io.IOException;

final class TokenRequest implements Take {

    private final SigninSheet sheet;
    TokenRequest(final SigninSheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Response act(final Request req) throws IOException {
        // TODO: Get the token out and check if valid.
        return null;
    }
}

package robhopkins.wc.iam.rest;

import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsEmpty;
import robhopkins.wc.iam.rest.exception.BadRequest;
import robhopkins.wc.iam.signin.SigninSheet;

import java.io.IOException;

public final class SignoutRequest implements Take {

    private final SigninSheet sheet;

    SignoutRequest(final SigninSheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Response act(final Request req) throws IOException {
        System.out.println("Signout Request");

        try {
            sheet.signout(AuthorizationToken.from(req));
        } catch (BadRequest e) {
            return e.toResponse();
        }

        return new RsEmpty();
    }
}
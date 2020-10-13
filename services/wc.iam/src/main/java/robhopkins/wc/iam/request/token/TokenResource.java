package robhopkins.wc.iam.request.token;

import robhopkins.wc.iam.request.RequestToken;
import robhopkins.wc.iam.sheet.SigninSheet;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/iam/token")
public class TokenResource {
    private final SigninSheet sheet;
    private final RequestToken ctx;

    @Inject
    public TokenResource(final SigninSheet sheet, final RequestToken ctx) {
        this.sheet = sheet;
        this.ctx = ctx;
    }

    @POST
    public Response signout() {
        // TODO: Get the token from the context and then determine what we want
        //  to check.  Definitely check exp, user exists and logged in?
        //  We can ask the sheet if user is signed in?
        return Response.ok().build();
    }
}

package robhopkins.wc.iam.request.signout;

import robhopkins.wc.iam.request.RequestToken;
import robhopkins.wc.iam.sheet.SigninSheet;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/iam/signout")
public final class SignoutResource {

    private final SigninSheet sheet;
    private final RequestToken ctx;

    @Inject
    public SignoutResource(final SigninSheet sheet, final RequestToken ctx) {
        this.sheet = sheet;
        this.ctx = ctx;
    }

    @POST
    public Response signout() {
        sheet.signout(ctx.toToken());
        return Response.ok().build();
    }
}

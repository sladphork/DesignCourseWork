package robhopkins.wc.iam.request.signin;

import org.jboss.resteasy.annotations.Form;
import robhopkins.wc.iam.request.exception.AuthenticationException;
import robhopkins.wc.iam.request.exception.ErrorResponseBuilder;
import robhopkins.wc.iam.sheet.SigninSheet;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("/iam/signin")
public class SigninResource {

    private final SigninSheet sheet;

    @Inject
    public SigninResource(final SigninSheet sheet) {
        this.sheet = sheet;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response signin(@Form SigninForm signinForm) {
        try {
            return SigninResponseBuilder.newBuilder()
                .withToken(sheet.signin(signinForm.toCredentials()))
                .build();
        } catch (AuthenticationException e) {
            return ErrorResponseBuilder.newBuilder()
                .withStatus(Response.Status.UNAUTHORIZED.getStatusCode())
                .withMessage(e.getMessage())
                .build();
        }
    }
}


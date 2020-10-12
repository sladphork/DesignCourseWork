package robhopkins.wc.iam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This is a Resource or Controller as defined by Quarkus
 * We'
 */
@Path("/hello")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

//    @POST
////    @Produces(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.TEXT_PLAIN)
//    public String signin(@Form SigninForm signinForm) {
//        return "Successfully logged in: " + signinForm.username;
//    }
}
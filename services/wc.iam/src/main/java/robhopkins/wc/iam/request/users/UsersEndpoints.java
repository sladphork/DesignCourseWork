package robhopkins.wc.iam.request.users;

import org.json.JSONArray;
import org.json.JSONObject;
import robhopkins.wc.iam.request.exception.IAMException;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.Users;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.UserNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/iam/users")
public class UsersEndpoints {

    @Inject
    Users users;

    @GET
    public Response getUsers() {
        try {
            final JSONArray json = new JSONArray();
            users.getAll().forEach(user -> json.put(toJson(user)));
            return Response.ok(json.toString(2))
                .header("Content-type", "application/vnd.wc.v1+json")
                .build();
        } catch (IAMException e) {
            return null;
        }
    }

    @GET
    @Path("/{username}")
    public Response getByUsername(@PathParam("username") final String username) {
        try {
            final User user = users.get(Username.from(username));
            return Response.ok(toJson(user).toString(2))
                .header("Content-type", "application/vnd.wc.v1+json")
                .build();
        } catch (UserNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (IAMException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/id:{id}")
    public Response getById(@PathParam("id") final String id) {
        try {
            final User user = users.get(UserId.from(id));
            return Response.ok(toJson(user).toString(2))
                .header("Content-type", "application/vnd.wc.v1+json")
                .build();
        } catch (UserNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (IAMException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response add(final String newUser) {
        try {
            final AddUser user = AddUser.from(newUser);
            return Response.status(201).entity(toJson(users.add(user.toUser())).toString(2))
                .build();
        } catch (IAMException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    private JSONObject toJson(final User user) {
        return new JSONObject()
            .put("id", user.id())
            .put("username", user.username())
            .put("role", user.role())
            .put("email", user.email());
    }
}

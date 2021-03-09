package robhopkins.wc.faculties.request;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import robhopkins.wc.faculties.Faculties;
import robhopkins.wc.faculties.Faculty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/faculties")
public final class FacultiesEndpoints {

    @ConfigProperty(name = "wc.db.url")
    String dbUrl;

    private final Faculties faculties;

    @Inject
    public FacultiesEndpoints(final Faculties faculties) {
        this.faculties = faculties;
    }

    @GET
    public Response getAll() {
        return execute(() ->
           Response.ok(
               toJson(faculties.getAll())
           )
        );
    }

    private String toJson(final Collection<Faculty> toMap) {
        return toMap.stream()
            .map(this::toJson)
            .collect(Collectors.joining(",", "[", "]"));
    }

    private String toJson(final Faculty faculty) {
        return new JsonPopulator(faculty).toString();
    }

    private Response execute(final Operation operation) {
        faculties.configure(Map.of("dburl", dbUrl));
        try {
            return operation.run()
                .header("Content-Type", "application/vnd.wc.v1+json")
                .build();
        } catch (Exception e) {
            // TODO
            return Response.serverError().build();
        }
    }

    private interface Operation {
        Response.ResponseBuilder run();
    }
}

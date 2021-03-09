package robhopkins.wc.professors.request;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorException;
import robhopkins.wc.professors.faculty.Faculties;
import robhopkins.wc.professors.iam.IAM;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/professors")
public final class ProfessorsEndpoints {

    @ConfigProperty(name = "wc.db.url")
    String dbUrl;

    private final IAM iam;
    private final Professors professors;
    private final Faculties faculties;

    @Inject
    public ProfessorsEndpoints(final Professors professors, final Faculties faculties, final IAM iam) {
        this.professors = professors;
        this.faculties = faculties;
        this.iam = iam;
    }

    @POST
    public Response add(final String professor) {
        return execute(() -> {
            final AddProfessor toAdd = AddProfessor.from(professor);
            faculties.checkDepartment(toAdd.departmentId());

            return addResponse(
                professors.add(toAdd.toProfessor())
            );
        });
    }

    private Response.ResponseBuilder addResponse(final Professor added) {
        try {
            final URI location = new URI(String.format("/professors/%s", added.id()));
            return Response.created(location)
                .entity(toJson(added));
        } catch (URISyntaxException e) {
            return Response.status(201)
                .entity(added);
        }
    }

    @GET
    public Response getAll() {
        return execute(() ->
           Response.ok(
               toJson(professors.getAll())
           )
        );
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") final String id) {
        return execute(() ->
            Response.ok(
                toJson(professors.get(ObjectId.from(id)))
            )
        );
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final String id, final String professor) {
        return execute(() -> {
            final UpdateProfessor update = UpdateProfessor.from(ObjectId.from(id), professor);
            faculties.checkDepartment(update.departmentId());
            return Response.ok(
                toJson(professors.update(update.toProfessor()))
            );
        });
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final String id) {
        return execute(() -> {
            professors.delete(ObjectId.from(id));
            return Response.noContent();
        });
    }

    private String toJson(final Collection<Professor> professors) {
        return professors.stream()
            .map(this::toJson)
            .collect(Collectors.joining(",", "[", "]"));
    }

    private String toJson(final Professor professor) {
        return new JsonPopulator(professor).toString();
    }

    private Response execute(final Operation operation) {
        professors.configure(Map.of("dburl", dbUrl));
        try {
            iam.validate("registrar");
            return operation.run()
                .header("Content-Type", "application/vnd.wc.v1+json")
                .build();
        } catch (ProfessorException e) {
            return e.toResponse();
        }
    }

    private interface Operation {
        Response.ResponseBuilder run() throws ProfessorException;
    }
}

package robhopkins.wc.students.request;

import robhopkins.wc.students.Student;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.StudentException;
import robhopkins.wc.students.faculty.Faculties;
import robhopkins.wc.students.iam.IAM;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("/students")
public final class StudentsEndpoints {

    private final IAM iam;
    private final Students students;
    private final Faculties faculties;

    @Inject
    public StudentsEndpoints(final Students students, final Faculties faculties, final IAM iam) {
        this.students = students;
        this.faculties = faculties;
        this.iam = iam;
    }

    @PATCH
    public Response addAll() {
        // TODO: implement to get the students into the db.
        return Response.status(405).entity("Coming Soon!").build();
    }

    @POST
    public Response add(final String student) {
        return execute(() -> {
            final AddStudent toAdd = AddStudent.from(student);
            faculties.check(toAdd.facultyId());

            return addResponse(
                students.add(toAdd.toStudent())
            );
        });
    }

    private Response.ResponseBuilder addResponse(final Student added) {
        try {
            final URI location = new URI(String.format("/students/%s", added.id()));
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
               toJson(students.getAll())
           )
        );
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") final String id) {
        return execute(() ->
            Response.ok(
                toJson(students.get(ObjectId.from(id)))
            )
        );
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final String id, final String student) {
        return execute(() -> {
            final UpdateStudent update = UpdateStudent.from(ObjectId.from(id), student);
            faculties.check(update.facultyId());
            return Response.ok(
                toJson(students.update(update.toStudent()))
            );
        });
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final String id) {
        return execute(() -> {
            students.delete(ObjectId.from(id));
            return Response.noContent();
        });
    }

    private String toJson(final Collection<Student> students) {
        return students.stream()
            .map(this::toJson)
            .collect(Collectors.joining(",", "[", "]"));
    }

    private String toJson(final Student student) {
        return new JsonPopulator(student).toString();
    }

    private Response execute(final Operation operation) {
        try {
            iam.validate("registrar");
            return operation.run()
                .header("Content-Type", "application/vnd.wc.v1+json")
                .build();
        } catch (StudentException e) {
            return e.toResponse();
        }
    }

    private interface Operation {
        Response.ResponseBuilder run() throws StudentException;
    }
}

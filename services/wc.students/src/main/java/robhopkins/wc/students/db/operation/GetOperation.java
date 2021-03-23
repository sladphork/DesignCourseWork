package robhopkins.wc.students.db.operation;

import org.json.JSONArray;
import org.json.JSONObject;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.common.datasource.QueryRequestBuilder;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.StudentBuilder;
import robhopkins.wc.students.db.DatasourceService;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.ServerException;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.nonNull;

final class GetOperation implements Operation<Collection<Student>> {

    private final ObjectId studentId;
    private final DatasourceService service;

    GetOperation(final ObjectId studentId, final DatasourceService service) {
        this.service = service;
        this.studentId = studentId;
    }

    @Override
    public Collection<Student> execute() throws ServerException {
        final QueryRequestBuilder builder = QueryRequestBuilder.newBuilder()
            .withNamespace("students")
            .forTable("students");
        if (addId()) {
            builder.where("id", studentId);
        }
        final DatasourceResponse response = service.execute(builder.build());

        final String students = response.body();
        final JSONArray result = new JSONArray(students);
        return StreamSupport.stream(result.spliterator(), false)
            .map(this::mapFromDB)
            .collect(Collectors.toList());
    }

    private boolean addId() {
        return nonNull(studentId);
    }

    private Student mapFromDB(final Object val) {
        final JSONObject obj = (JSONObject) val;
        return StudentBuilder.newBuilder()
            .withFacultyId(ObjectId.from(obj.getString("FACULTY_ID")))
            .withFirstName(obj.getString("FIRST_NAME"))
            .withLastName(obj.getString("LAST_NAME"))
            .withId(ObjectId.from(obj.getString("ID")))
            .withEmail(obj.getString("EMAIL"))
            .build();
    }
}

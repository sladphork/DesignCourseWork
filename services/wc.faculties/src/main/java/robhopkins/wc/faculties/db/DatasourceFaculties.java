package robhopkins.wc.faculties.db;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import robhopkins.wc.faculties.*;
import robhopkins.wc.faculties.domain.ObjectId;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class DatasourceFaculties implements Faculties {

    @Override
    public void configure(final Map<String, Object> properties) {
        RestAssured.baseURI = (String) properties.get("dburl");
    }

    @Override
    public Collection<Faculty> getAll() {
        final JSONObject request = new JSONObject()
            .put("namespace", "faculties")
            .put("options", facultyOptions())
            .put("type", "QUERY");

        final Response response = execute(request);
        final String faculties = response.asString();
        final JSONArray result = new JSONArray(faculties);
        return StreamSupport.stream(result.spliterator(), false)
            .map(this::mapFromDB)
            .collect(Collectors.toList());
    }

    private Faculty mapFromDB(final Object val) {
        final JSONObject obj = (JSONObject) val;
        final ObjectId id = ObjectId.from(obj.getString("ID"));
        return FacultyBuilder.newBuilder()
            .withId(id)
            .withName(obj.getString("NAME"))
            .withDepartments(departmentsFor(id))
            .build();
    }

    private Collection<Department> departmentsFor(final ObjectId id) {
        final JSONObject request = new JSONObject()
            .put("namespace", "faculties")
            .put("options", departmentOptions(id))
            .put("type", "QUERY");

        final Response response = execute(request);
        final String departments = response.asString();
        final JSONArray result = new JSONArray(departments);
        return StreamSupport.stream(result.spliterator(), false)
            .map(obj -> {
                final JSONObject json = (JSONObject)obj;
                return DepartmentBuilder.newBuilder()
                    .withId(ObjectId.from(json.getString("ID")))
                    .withName(json.getString("NAME"))
                    .build();
            })
            .collect(Collectors.toList());
    }

    private JSONObject facultyOptions() {
        final JSONObject options = new JSONObject()
            .put("table", "faculties");
        return options;
    }

    private JSONObject departmentOptions(final ObjectId facultyId) {
        final JSONObject options = new JSONObject()
            .put("table", "departments")
            .put("where", new JSONObject()
                .put("field", "faculty_id")
                .put("value", facultyId.toString())
            );
        return options;
    }

    private Response execute(final Object body) {
        return RestAssured.given()
            .body(body.toString())
            .header("Content-Type", "application/json")
            .post("/data");
    }
}

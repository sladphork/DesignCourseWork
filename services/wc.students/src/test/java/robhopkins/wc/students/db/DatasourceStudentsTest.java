package robhopkins.wc.students.db;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import robhopkins.wc.common.datasource.DatasourceResponse;
import robhopkins.wc.students.Student;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.StudentNotFoundException;
import robhopkins.wc.students.request.UpdateStudent;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsIterableContaining.hasItems;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.fail;
import static robhopkins.wc.students.db.Matchers.hasPayload;
import static robhopkins.wc.students.db.Matchers.student;

final class DatasourceStudentsTest {

    private static final JSONObject STUDENT_1 = new JSONObject()
        .put("ID", "12345")
        .put("FIRST_NAME", "STUDENT")
        .put("LAST_NAME", "ONE")
        .put("FACULTY_ID", "12345")
        .put("EMAIL", "student.one@test.com");
    private static final JSONObject STUDENT_2 = new JSONObject()
        .put("ID", "34567")
        .put("FIRST_NAME", "STUDENT")
        .put("LAST_NAME", "TWO")
        .put("FACULTY_ID", "98765")
        .put("EMAIL", "student.two@test.com");

    @Test
    void getAllShouldReturnCollection() throws Exception {
        final DatasourceServiceMock datasourceService = new DatasourceServiceMock(
            responseForGet(STUDENT_1, STUDENT_2)
        );
        final Students students = new DatasourceStudents(datasourceService);

        final Collection<Student> result = students.getAll();
        assertThat(result.size(), is(2));
        assertThat(result, hasItems(student(STUDENT_1), student(STUDENT_2)));
        assertThat(datasourceService, hasPayload("students", "students"));
    }

    @Test
    void getShouldThrowExceptionIfNotFound() throws Exception {
        final DatasourceServiceMock datasourceService = new DatasourceServiceMock(
            responseForGet()
        );
        final Students students = new DatasourceStudents(datasourceService);

        try {
            students.get(ObjectId.from("12345"));
            fail("Expected exception not thrown!");
        } catch (StudentNotFoundException e) {
            assertThat(e.getMessage(), containsString("12345"));
        }
    }

    @Test
    void getShouldReturnStudent() throws Exception {
        final DatasourceServiceMock datasourceService = new DatasourceServiceMock(
            responseForGet(STUDENT_1)
        );
        final Students students = new DatasourceStudents(datasourceService);

        final Student found = students.get(ObjectId.from("12345"));
        assertThat(found, is(student(STUDENT_1)));
        assertThat(datasourceService, hasPayload("students", "students",
            options -> {
                final JSONObject where = options.getJSONObject("where");
                return "id".equals(where.getString("field"))
                    && "12345".equals(where.getString("value"));
            })
        );
    }

    @Test
    void addShouldReturnStudent() throws Exception {
        final DatasourceServiceMock datasourceService = new DatasourceServiceMock(
            responseForGet(new JSONObject().put("result", "success"))
        );
        final Students students = new DatasourceStudents(datasourceService);
        final Student toAdd = new TestStudent(STUDENT_2);

        final Student added = students.add(toAdd);
        assertThat(added, is(student(STUDENT_2)));
        assertThat(datasourceService, hasPayload("students", "students",
            options -> {
                final JSONObject values = options.getJSONObject("values");
                return "34567".equals(values.getString("id"))
                    && "98765".equals(values.getString("faculty_id"))
                    && "STUDENT".equals(values.getString("first_name"))
                    && "TWO".equals(values.getString("last_name"))
                    && "student.two@test.com".equals(values.getString("email"));
            })
        );
    }

    @Test
    void updateShouldReturnStudent() throws Exception {
        final JSONObject dsUpdated = new JSONObject(STUDENT_1.toMap())
            .put("FIRST_NAME", "Update")
            .put("LAST_NAME", "Test");
        final DatasourceServiceMock datasourceService = new DatasourceServiceMock(
            request -> "UPDATE".equals(request.type()),
            responseForGet(STUDENT_1),
            responseForGet(new JSONObject().put("result", "success")),
            responseForGet(dsUpdated)

        );
        final Students students = new DatasourceStudents(datasourceService);
        final UpdateStudent updateStudent = UpdateStudent.from(ObjectId.from("12345"),
            new JSONObject()
                .put("firstName", "Update")
                .put("lastName", "Test")
                .toString()
        );

        final Student updated = students.update(updateStudent.toStudent());
        assertThat(updated, is(student(dsUpdated)));
        assertThat(datasourceService, hasPayload("students", "students",
            options -> {
                final JSONObject values = options.getJSONObject("values");
                return "Update".equals(values.getString("first_name"))
                    && "Test".equals(values.getString("last_name"))
                    && "12345".equals(options.getJSONObject("id").get("id").toString());
            })
        );
    }

    @Test
    void updateShouldThrowExceptionIfNotFound() throws Exception {
        final DatasourceServiceMock datasourceService = new DatasourceServiceMock(
            responseForGet()
        );
        final Students students = new DatasourceStudents(datasourceService);

        final UpdateStudent updateStudent = UpdateStudent.from(ObjectId.from("12345"),
            new JSONObject()
                .put("firstName", "Update")
                .put("lastName", "Test")
                .toString()
        );
        try {
            students.update(updateStudent.toStudent());
            fail("Expected exception not thrown!");
        } catch (StudentNotFoundException e) {
            assertThat(e.getMessage(), containsString("12345"));
        }
    }

    @Test
    void deleteShouldSuccessfullyComplete() throws Exception {
        final DatasourceServiceMock datasourceService = new DatasourceServiceMock();
        final Students students = new DatasourceStudents(datasourceService);

        students.delete(ObjectId.from("12345"));
        assertThat(datasourceService, hasPayload("students", "students",
            options -> {
                final JSONObject id = options.getJSONObject("id");
                return "12345".equals(id.get("id").toString());
            })
        );
    }

    private DatasourceResponse responseForGet(final JSONObject... students) {
        return new DatasourceResponse() {
            @Override
            public int status() {
                return 200;
            }

            @Override
            public String body() {
                return new JSONArray(Arrays.asList(students))
                    .toString(2);
            }
        };
    }

}

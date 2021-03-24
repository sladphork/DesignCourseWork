package robhopkins.wc.students.request;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import robhopkins.wc.students.MockStudents;
import robhopkins.wc.students.StudentBuilder;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.faculty.Faculties;
import robhopkins.wc.students.faculty.exception.FacultyNotFoundException;
import robhopkins.wc.students.iam.IAM;

import java.util.Arrays;
import java.util.function.Function;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;

@QuarkusTest
final class UpdateTest {
    private static final String END_POINT = "/students/{id}";
    private static final String TEST_ID = "54321";

    private Students students;
    private Faculties faculties;
    private IAM iam;

    @BeforeEach
    void createTestObjects() {
        students = new MockStudents();
        faculties = new MockFaculties();
        iam = new MockIAM();

        MockStudents.put(
            StudentBuilder.newBuilder()
                .withFirstName("Test")
                .withLastName("Professor")
                .withId(ObjectId.from(TEST_ID))
                .withFacultyId(ObjectId.from("12345"))
                .build()
        );
    }

    @AfterEach
    void cleanUp() {
        MockStudents.clear();
        MockFaculties.clear();
        MockIAM.clear();
    }

    @Test
    void requestShouldReturn401IfTokenInvalid() {
        MockIAM.throwException();
        test(401,
            createBody(),
            "application/vnd.wc.error.v1+text",
            is("User is unauthorized.")
        );
    }

    @Test
    void requestShouldReturn403IfTokenInvalid() {
        MockIAM.throwForbidden();
        test(403,
            createBody(),
            "application/vnd.wc.error.v1+text",
            is("User is forbidden from performing this operation.")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"firstName,lastName,facultyId", "firstName", "lastName", "facultyId"})
    void requestShouldReturn200IfWithMissingFields(final String fields) {
        test(200,
            createBody(json -> removeFields(json, fields)),
            "application/vnd.wc.v1+json",
            isExpectedStudent()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"firstName", "lastName", "facultyId"})
    void requestShouldReturn200IfWithEmptyFields(final String field) {
        test(200,
            createBody(json -> json.put(field, " ")),
            "application/vnd.wc.v1+json",
            isExpectedStudent()
        );
    }

    @Test
    void requestShouldReturn404IfFacultyNotFound() {
        MockFaculties.throwNotFound(new FacultyNotFoundException("12345"));
        test(404,
            createBody(),
            "application/vnd.wc.error.v1+text",
            containsString("Unable to find faculty: '12345'")
        );
    }

    @Test
    void requestShouldReturnUpdatedStudent() {
        test(200,
            createBody(
                json -> json.put("firstName", "Update")
                    .put("lastName", "ToStudent")
                    .put("facultyId", "67890")
            ),
            "application/vnd.wc.v1+json",
            isExpectedStudent("Update", "ToStudent", "67890"));
    }

    private Matcher<?> isExpectedStudent() {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final JSONObject json = new JSONObject((String)o);
                return "Test".equals(json.getString("firstName"))
                    && "Professor".equals(json.getString("lastName"))
                    && "12345".equals(json.getString("facultyId"))
                    && TEST_ID.equals(json.getString("id"));
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    private Matcher<?> isExpectedStudent(final String firstName, final String lastName, final String facultyId) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final JSONObject json = new JSONObject((String)o);
                return firstName.equals(json.getString("firstName"))
                    && lastName.equals(json.getString("lastName"))
                    && facultyId.equals(json.getString("facultyId"))
                    && TEST_ID.equals(json.getString("id"));
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    private void test(final int expectedStatus, final Object body, final String expectedContentType, final Matcher<?> expectedBody) {
        given()
            .body(body)
            .contentType(ContentType.JSON)
            .when().put(END_POINT, TEST_ID)
            .then()
            .statusCode(expectedStatus)
            .contentType(expectedContentType)
            .body(expectedBody);
    }

    private String createBody() {
        return createBody(Function.identity());
    }

    private String createBody(final Function<JSONObject, JSONObject> mapper) {
        return mapper.apply(
            new JSONObject()
                .put("firstName", "Test")
                .put("lastName", "Professor")
                .put("facultyId", "12345")
            ).toString(2);
    }

    private JSONObject removeFields(final JSONObject json, final String fields) {
        Arrays.stream(fields.split(","))
            .forEach(json::remove);
        return json;
    }
}

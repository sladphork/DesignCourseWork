package robhopkins.wc.professors.request;

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
import robhopkins.wc.professors.MockProfessors;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.faculty.Faculties;
import robhopkins.wc.professors.faculty.exception.DepartmentNotFoundException;
import robhopkins.wc.professors.faculty.exception.FacultyNotFoundException;
import robhopkins.wc.professors.iam.IAM;

import java.util.Arrays;
import java.util.function.Function;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
final class AddTest {
    private static final String END_POINT = "/professors";

    private Professors professors;
    private Faculties faculties;
    private IAM iam;

    @BeforeEach
    void createTestObjects() {
        professors = new MockProfessors();
        faculties = new MockFaculties();
        iam = new MockIAM();
    }

    @AfterEach
    void cleanUp() {
        MockProfessors.clear();
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
    @ValueSource(strings = {"firstName,lastName,departmentId", "firstName", "lastName", "departmentId"})
    void requestShouldReturn400IfWithMissingFields(final String fields) {
        test(400,
            createBody(json -> removeFields(json, fields)),
            "application/vnd.wc.error.v1+text",
            missingFields("AddProfessor", "fields")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"firstName", "lastName", "departmentId"})
    void requestShouldReturn400IfWithEmptyFields(final String field) {
        test(400,
            createBody(json -> json.put(field, " ")),
            "application/vnd.wc.error.v1+text",
            missingFields("AddProfessor", "fields")
        );
    }

    @Test
    void requestShouldReturn404IfDepartmentIdNotFound() {
        MockFaculties.throwNotFound(new DepartmentNotFoundException("12345"));
        test(404,
            createBody(),
            "application/vnd.wc.error.v1+text",
            containsString("Unable to find department: '12345'")
        );
    }

    @Test
    void requestShouldReturnNewProfessor() {
        test(201,
            createBody(),
            "application/vnd.wc.v1+json",
            isExpectedProfessor());
    }

    private Matcher<?> isExpectedProfessor() {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final JSONObject json = new JSONObject((String)o);
                return "Test".equals(json.getString("firstName"))
                    && "Professor".equals(json.getString("lastName"))
                    && "12345".equals(json.getString("departmentId"))
                    && "Test.Professor@wc.edu".equalsIgnoreCase(json.getString("email"))
                    && json.has("id");
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
            .when().post(END_POINT)
            .then()
            .statusCode(expectedStatus)
            .contentType(expectedContentType)
            .body(expectedBody);
    }

    private Matcher<String> missingFields(final String type, final String fields) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final String message = (String)o;
                return message.contains(type)
                    && Arrays.stream(fields.split(","))
                    .anyMatch(message::contains);
            }

            @Override
            public void describeTo(final Description description) {
            }
        };
    }
    private String createBody() {
        return createBody(Function.identity());
    }

    private String createBody(final Function<JSONObject, JSONObject> mapper) {
        return mapper.apply(
            new JSONObject()
                .put("firstName", "Test")
                .put("lastName", "Professor")
                .put("departmentId", "12345")
            ).toString(2);
    }

    private JSONObject removeFields(final JSONObject json, final String fields) {
        Arrays.stream(fields.split(","))
            .forEach(json::remove);
        return json;
    }
}

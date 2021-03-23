package robhopkins.wc.students.request;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robhopkins.wc.students.MockStudents;
import robhopkins.wc.students.StudentBuilder;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.exception.StudentNotFoundException;
import robhopkins.wc.students.faculty.Faculties;
import robhopkins.wc.students.iam.IAM;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
final class GetByIdTest {

    private static final String TEST_ID = "12345";
    private static final String END_POINT = "/students/{id}";

    private Students students;
    private IAM iam;
    private Faculties faculties;

    @BeforeEach
    void createTestObjects() {
        students = new MockStudents();
        iam = new MockIAM();
        faculties = new MockFaculties();

        MockStudents.put(
            StudentBuilder.newBuilder()
                .withFirstName("Test")
                .withLastName("Student")
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
            "application/vnd.wc.error.v1+text",
            is("User is unauthorized.")
        );
    }

    @Test
    void requestShouldReturn403IfTokenInvalid() {
        MockIAM.throwForbidden();
        test(403,
            "application/vnd.wc.error.v1+text",
            is("User is forbidden from performing this operation.")
        );
    }

    @Test
    void requestShouldReturn404IfNotFound() {
        MockStudents.throwNotFoundException(
            new StudentNotFoundException(ObjectId.from(TEST_ID))
        );
        test(404,
            "application/vnd.wc.error.v1+text",
            is("Unable to find professor: '12345'")
        );
    }

    @Test
    void requestShouldReturnStudent() {
        test(200,
            "application/vnd.wc.v1+json",
            isExpectedStudent()
        );
    }

    private Matcher<?> isExpectedStudent() {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final JSONObject json = new JSONObject((String)o);
                return "Test".equals(json.getString("firstName"))
                    && "Student".equals(json.getString("lastName"))
                    && "12345".equals(json.getString("facultyId"))
                    && TEST_ID.equals(json.getString("id"));
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    private void test(final int expectedStatus, final String expectedContentType, final Matcher<?> expectedBody) {
        given()
            .when().get(END_POINT, TEST_ID)
            .then()
            .statusCode(expectedStatus)
            .contentType(expectedContentType)
            .body(expectedBody);
    }
}

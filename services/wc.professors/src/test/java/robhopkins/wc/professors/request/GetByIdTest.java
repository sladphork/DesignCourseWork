package robhopkins.wc.professors.request;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robhopkins.wc.professors.MockProfessors;
import robhopkins.wc.professors.ProfessorBuilder;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;
import robhopkins.wc.professors.faculty.Faculties;
import robhopkins.wc.professors.iam.IAM;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
final class GetByIdTest {

    private static final String TEST_ID = "12345";
    private static final String END_POINT = "/professors/{id}";

    private Professors professors;
    private IAM iam;
    private Faculties faculties;

    @BeforeEach
    void createTestObjects() {
        professors = new MockProfessors();
        iam = new MockIAM();
        faculties = new MockFaculties();

        MockProfessors.put(
            ProfessorBuilder.newBuilder()
                .withFirstName("Test")
                .withLastName("Professor")
                .withId(ObjectId.from(TEST_ID))
                .withDepartmentId(ObjectId.from("12345"))
                .build()
        );
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
        MockProfessors.throwNotFoundException(
            new ProfessorNotFoundException(ObjectId.from(TEST_ID))
        );
        test(404,
            "application/vnd.wc.error.v1+text",
            is("Unable to find professor: '12345'")
        );
    }

    @Test
    void requestShouldReturnProfessor() {
        test(200,
            "application/vnd.wc.v1+json",
            isExpectedProfessor()
        );
    }

    private Matcher<?> isExpectedProfessor() {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final JSONObject json = new JSONObject((String)o);
                return "Test".equals(json.getString("firstName"))
                    && "Professor".equals(json.getString("lastName"))
                    && "12345".equals(json.getString("departmentId"))
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

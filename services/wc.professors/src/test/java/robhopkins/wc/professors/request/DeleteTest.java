package robhopkins.wc.professors.request;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robhopkins.wc.professors.MockProfessors;
import robhopkins.wc.professors.ProfessorBuilder;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.faculty.Faculties;
import robhopkins.wc.professors.iam.IAM;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
final class DeleteTest {

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
                .withFacultyId(ObjectId.from("12345"))
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
    void requestShouldReturn204IfSuccessful() {
        MockProfessors.put(
            ProfessorBuilder.newBuilder()
                .withFirstName("Test")
                .withLastName("Professor")
                .withId(ObjectId.from(TEST_ID))
                .withFacultyId(ObjectId.from("12345"))
                .build()
        );

        test(204,
            "",
            is(""));
    }

    @Test
    void requestShouldReturn204IfNotFound() {

        test(204,
            "",
            is(""));
    }

    private void test(final int expectedStatus, final String expectedContentType, final Matcher<?> expectedBody) {
        given()
            .when().delete(END_POINT, TEST_ID)
            .then()
            .statusCode(expectedStatus)
            .contentType(expectedContentType)
            .body(expectedBody);
    }
}

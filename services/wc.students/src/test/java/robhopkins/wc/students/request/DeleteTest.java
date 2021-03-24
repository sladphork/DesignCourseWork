package robhopkins.wc.students.request;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robhopkins.wc.students.MockStudents;
import robhopkins.wc.students.StudentBuilder;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.faculty.Faculties;
import robhopkins.wc.students.iam.IAM;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
final class DeleteTest {

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
    void requestShouldReturn204IfSuccessful() {
        MockStudents.put(
            StudentBuilder.newBuilder()
                .withFirstName("Test")
                .withLastName("Student")
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

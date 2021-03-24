package robhopkins.wc.students.request;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robhopkins.wc.students.MockStudents;
import robhopkins.wc.students.StudentBuilder;
import robhopkins.wc.students.Students;
import robhopkins.wc.students.domain.ObjectId;
import robhopkins.wc.students.faculty.Faculties;
import robhopkins.wc.students.iam.IAM;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
final class GetAllTest {
    private static final String END_POINT = "/students";

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
    void requestShouldReturn200AndEmptyIfNoStudents() {
        test(200,
            "application/vnd.wc.v1+json",
            is("[]")
        );
    }

    @Test
    void requestShouldReturn200AndAllProfessors() {
        MockStudents.put(
            StudentBuilder.newBuilder()
                .withFirstName("First")
                .withLastName("Student")
                .withId(ObjectId.from("23456"))
                .withFacultyId(ObjectId.from("12345"))
                .build()
        );
        MockStudents.put(
            StudentBuilder.newBuilder()
                .withFirstName("Second")
                .withLastName("Student")
                .withId(ObjectId.from("98765"))
                .withFacultyId(ObjectId.from("12345"))
                .build()
        );

        test(200,
            "",
            containsStudents("23456", "98765")
        );
    }

    private Matcher<String> containsStudents(final String... ids) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(final Object o) {
                final JSONArray items = new JSONArray((String)o);
                if (items.length() != ids.length) {
                    return false;
                }
                for (Object item: items) {
                    final JSONObject json = (JSONObject)item;
                    if (Arrays.binarySearch(ids, json.getString("id")) < 0) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void describeTo(final Description description) {

            }
        };
    }

    private void test(final int expectedStatus, final String expectedContentType, final Matcher<?> expectedBody) {
        given()
            .when().get(END_POINT)
            .then()
            .statusCode(expectedStatus)
            .contentType(expectedContentType)
            .body(expectedBody);
    }
}

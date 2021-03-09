package robhopkins.wc.professors.request;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robhopkins.wc.professors.MockProfessors;
import robhopkins.wc.professors.ProfessorBuilder;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.faculty.Faculties;
import robhopkins.wc.professors.iam.IAM;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
final class GetAllTest {
    private static final String END_POINT = "/professors";

    private Professors professors;
    private IAM iam;
    private Faculties faculties;

    @BeforeEach
    void createTestObjects() {
        professors = new MockProfessors();
        iam = new MockIAM();
        faculties = new MockFaculties();
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
    void requestShouldReturn200AndEmptyIfNoProfessors() {
        test(200,
            "application/vnd.wc.v1+json",
            is("[]")
        );
    }

    @Test
    void requestShouldReturn200AndAllProfessors() {
        MockProfessors.put(
            ProfessorBuilder.newBuilder()
                .withFirstName("First")
                .withLastName("Professor")
                .withId(ObjectId.from("23456"))
                .withDepartmentId(ObjectId.from("12345"))
                .build()
        );
        MockProfessors.put(
            ProfessorBuilder.newBuilder()
                .withFirstName("Second")
                .withLastName("Professor")
                .withId(ObjectId.from("98765"))
                .withDepartmentId(ObjectId.from("12345"))
                .build()
        );

        test(200,
            "",
            containsProfessors("23456", "98765")
        );
    }

    private Matcher<String> containsProfessors(final String... ids) {
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
            public void describeTo(Description description) {

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

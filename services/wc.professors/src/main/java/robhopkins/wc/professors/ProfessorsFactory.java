package robhopkins.wc.professors;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import robhopkins.wc.professors.db.DatasourceProfessors;
import robhopkins.wc.professors.db.DatasourceRequest;
import robhopkins.wc.professors.db.EventProfessors;

public final class ProfessorsFactory {
    public static ProfessorsFactory newFactory() {
        return new ProfessorsFactory();
    }

    private ProfessorsFactory() {
    }

    public Professors create() {
        // We can control this based on config options, etc.
        return new EventProfessors(new DatasourceProfessors(new RestAssuredRequest()));
    }

    private static final class RestAssuredRequest implements DatasourceRequest {

        @Override
        public Response execute(final Object body) {
            return RestAssured.given()
                .body(body.toString())
                .header("Content-Type", "application/json")
                .post("/data");
        }
    }
}

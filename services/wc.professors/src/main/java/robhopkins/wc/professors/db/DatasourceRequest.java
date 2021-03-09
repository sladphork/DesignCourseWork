package robhopkins.wc.professors.db;

import io.restassured.response.Response;

public interface DatasourceRequest {

    /**
     * TODO: We will use RestAssured response for now, but once we're happy we will change it.
     * @param body
     * @return
     */
    Response execute(Object body);
}

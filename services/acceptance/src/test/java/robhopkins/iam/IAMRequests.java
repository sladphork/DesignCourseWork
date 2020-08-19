package robhopkins.iam;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.assertj.core.api.Assertions.fail;

public final class IAMRequests {
    public static IAMRequests newInstance(final int port) {
        return new IAMRequests(String.format("http://localhost:%d", port));
    }

    private IAMRequests(final String baseUri) {
        RestAssured.baseURI = baseUri;
    }

    public void testReady() {
        final RequestSpecification request = RestAssured.given()
            .header(origin());
        // TODO: make me a bit cleaner.
        int count = 1;
        do {
            final int status = executeReadyQuery(request);
            if (status != 200) {
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        } while (count < 11);
        fail("Service is not ready.");
    }

    private int executeReadyQuery(final RequestSpecification request) {
        return request.get("/canary").getStatusCode();
    }

    Response signin(final String username, final String secret) {
        return RestAssured.given()
            .body(signinForm(username, secret))
            .header(origin())
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post("/iam/signin");
    }

    private Object signinForm(final String username, final String secret) {
        return String.format(
            "username=%s&secret=%s",
            username,
            secret
        );
    }

    Response signout(final String token) {
        return RestAssured.given()
            .header(origin())
            .header("Authorization", "Bearer " + token)
            .post("/iam/signout");
    }

    Response token(final String token) {
        return RestAssured.given()
            .header(origin())
            .header("Authorization", "Bearer " + token)
            .post("/iam/token");
    }

    private Header origin() {
        return new Header("Origin", "http://localhost");
    }

}

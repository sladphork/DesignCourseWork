package robhopkins.iam;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static robhopkins.util.Conditions.*;

/**
 * Tests the '/iam/signin' endpoint.
 */
public class SigninSteps {

    private IAMRequests requests;
    private IAMService service;
    private String username;
    private String token;

    @Before
    public void start() {
        service = IAMService.newService();
        final int port = service.start();

        requests = IAMRequests.newInstance(port);
        requests.testReady();
    }

    @After
    public void stop() {
        service.stop();
    }


    @Given("^A user with (.*)$")
    public void aUserWithUsername(final String username) {
        this.username = username;
    }

    @When("^The user attempts sign in with valid (.*)$")
    public void theUserAttemptsSignInWithValidUsernameAndSecret(final String secret) {
        final Response response = requests.signin(username, secret);

        assertThat(response).is(okResponse("application/vnd.wc.v1+json"));

        final String payload = response.jsonPath().getString("token");
        assertThat(payload).is(validString());

        token = new String(Base64.getDecoder().decode(payload));
    }

    @Then("^The user has signed in and has a Token with (.*)$")
    public void theUserHasSignedInAndHasATokenWithRole(final String role) {
        final JSONObject json = new JSONObject(token);
        assertThat(json).has(fieldWithValue("role", role));
        assertThat(json).has(fieldWithValue("sub", username));
    }

    @When("The user attempts to signin with invalid secret")
    public void theUserAttemptsToSigninWithInvalidSecret() {
        testInvalid("invalid_secret");
    }

    @When("The user attempts to signin")
    public void theUserAttemptsToSignin() {
        testInvalid("unknown");
    }

    private void testInvalid(final String secret) {
        final Response response = requests.signin(username, secret);
        assertThat(response).is(unauthorized());

        try {
            response.jsonPath().getString("token");
            fail("Response as a Token.");
        } catch (JsonPathException e) {
            // Do nothing.
        }
    }

    @Then("The user does not have a Token.")
    public void theUserDoesNotHaveAToken() {
        assertThat(token).isNullOrEmpty();
    }
}

package robhopkins.iam;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static robhopkins.util.Conditions.okResponse;

public class SignoutSteps {

    private IAMRequests requests;
    private IAMService service;
    private String token;
    private Response response;

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

    @Given("^A (.*) with (.*) that has signed in$")
    public void aUsernameWithSecretThatHasSignedIn(final String username, final String secret) {
        signin(username, secret);
    }

    @When("The user attempts to sign out")
    public void theUserAttemptsToSignOut() {
        response = requests.signout(token);
    }

    @Then("User has signed out successfully")
    public void userHasSignedOutSuccessfully() {
        assertThat(response).is(okResponse());
    }

    private void signin(final String username, final String secret) {
        final Response response = requests.signin(username, secret);

        final String payload = response.jsonPath().getString("token");

        token = payload;
    }
}

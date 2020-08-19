package robhopkins.iam;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenSteps {

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

    @Given("a user attempting to check a token")
    public void aUserAttemptingToCheckAToken() {
        // Do nothing, we want this to make it a bit better to read.
    }

    @When("they call the endpoint")
    public void theyCallTheEndpoint() {
        response = requests.token(null);
    }

    @Then("they receive a Not Implemented Response")
    public void theyReceiveANotImplementedResponse() {
        assertThat(response.getStatusCode()).isEqualTo(501);
    }
}

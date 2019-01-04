package gradle.cucumber;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;

public class Steps {

    private String port;
    private Support server;
    private HttpResponse<String> response;

    @Given("The server is running on port {string}")
    public void theServerIsRunningOnPort(String port) {
        this.port = port;
        this.server = new Support(port);
        new Thread(server).start();
    }

    @When("I send method {string} for {string} with body {string} to host at the specified port")
    public void iSendMethodForWithBodyToHostAtTheSpecifiedPort(String method, String location, String body) throws IOException, InterruptedException {
        this.response = new Client().request(port, method, location, body);
    }

    @Then("I should receive a response with version {string}")
    public void iShouldReceive(String version) {
        assertEquals(version, response.version().toString());
    }

    @Then("Status code {int}")
    public void statusCode(int code) {
        assertEquals(code, response.statusCode());
    }

    @Then("Body {string}")
    public void body(String body) throws Throwable {
        assertEquals(body, response.body());
    }

    @After
    public void cleanup() {
        server.close();
    }

}

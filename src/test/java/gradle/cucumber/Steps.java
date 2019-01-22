package gradle.cucumber;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Steps {
    private List<HttpResponse<String>> responses = new ArrayList<>();
    private String port;

    private static boolean running = false;
    private Process process;

    @Before
    public void setup() throws IOException {
        if (!running) {
            process = new ProcessBuilder("java", "-jar", "build/libs/carnitas-1.0-SNAPSHOT.jar").start();

            Runtime.getRuntime().addShutdownHook(teardown());

            running = true;
        }
    }

    private Thread teardown() {
        return new Thread(process::destroy);
    }

    @Given("The server is running on port {string}")
    public void theServerIsRunningOnPort(String port) {
        this.port = port;
    }

    @When("I send method {string} for {string} to host at the specified port")
    public void iSendMethodForToHostAtTheSpecifiedPortTimes(String method, String location) throws IOException, InterruptedException {
        responses.add(new Client().request(port, method, location));
    }

    @When("I send method {string} for {string} with body {string} to host at the specified port")
    public void iSendMethodForWithBodyToHostAtTheSpecifiedPort(String method, String location, String body) throws IOException, InterruptedException {
        responses.add(
          new Client()
            .setBody(body)
            .request(port, method, location)
        );
    }

    @Then("I should receive a response with version {string}")
    public void iShouldReceive(String version) {
        assertEquals(version, responses.get(0).version().toString());
    }

    @Then("Status code {int}")
    public void statusCode(int code) {
        assertEquals(code, responses.get(0).statusCode());
    }

    @And("Body {string}")
    public void body(String body) {
        assertEquals(body, responses.get(0).body());
    }

    @Then("I should receive responses with version {string}")
    public void iShouldReceiveResponsesWithVersion(String version) {
        for (HttpResponse response : responses) {
            assertEquals(version, response.version().toString());
        }
    }

    @And("The {int}(st|nd) response should have status code {int}")
    public void theStResponseShouldHaveStatusCode(int index, int code) {
        assertEquals(code, responses.get(index - 1).statusCode());
    }

    @And("Status codes {int}")
    public void statusCodes(int code) {
        for (HttpResponse response : responses) {
            assertEquals(code, response.statusCode());
        }
    }

    @And("The {int}nd response should have body {string}")
    public void theNdResponseShouldHaveBody(int index, String body) {
        assertEquals(body, responses.get(index - 1).body());
    }

}

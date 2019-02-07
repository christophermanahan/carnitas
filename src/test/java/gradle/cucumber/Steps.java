package gradle.cucumber;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
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
    public void iSendMethodForToHostAtTheSpecifiedPortTimes(String method, String uri) throws IOException, InterruptedException {
        responses.add(new Client().request(port, method, uri));
    }

    @When("I send method {string} for {string} with body {string} to host at the specified port")
    public void iSendMethodForWithBodyToHostAtTheSpecifiedPort(String method, String uri, String body) throws IOException, InterruptedException {
        responses.add(
          new Client()
            .withBody(body)
            .request(port, method, uri)
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
    public void theIntResponseShouldHaveStatusCode(int index, int code) {
        assertEquals(code, responses.get(index - 1).statusCode());
    }

    @And("Status codes {int}")
    public void statusCodes(int code) {
        for (HttpResponse response : responses) {
            assertEquals(code, response.statusCode());
        }
    }

    @And("The {int}nd response should have body {string}")
    public void theIntResponseShouldHaveBody(int index, String body) {
        assertEquals(body, responses.get(index - 1).body());
    }

    @And("Header {string} with value(s) {string}")
    public void headerWithValues(String header, String values) {
        assertEquals(
          new HashSet<>(List.of(responses.get(0).headers().allValues(header).get(0).split(" "))),
          new HashSet<>(List.of(values.split(", ")))
        );
    }
}

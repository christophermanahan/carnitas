package gradle.cucumber;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.christophermanahan.carnitas.Main;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Steps {
    private List<HttpResponse<String>> responses = new ArrayList<>();
    private String port;

    private static boolean running = false;

    @Before
    public void BeforeAll() {
        if (!running) {
            String[] port = {"33333"};
            new Thread(() -> Main.main(port)).start();

            Runtime.getRuntime().addShutdownHook(AfterAll());

            running = true;
        }
    }

    private Thread AfterAll() {
        return new Thread(Main::stop);
    }

    @Given("The server is running on port {string}")
    public void theServerIsRunningOnPort(String port) {
        this.port = port;
    }

    @When("I send method {string} for {string} to host at the specified port {int} time(s)")
    public void iSendToHostAtTheSpecifiedPort(String method, String location, int times) throws IOException, InterruptedException {
        for (int i = 0; i < times; i++) {
            responses.add(new Client().request(port, method, location));
        }
    }

    @Then("I should receive responses with version {string}")
    public void iShouldReceive(String version) {
        for (HttpResponse response : responses) {
            assertEquals(version, response.version().toString());
        }
    }

    @Then("Status codes {int}")
    public void statusCode(int code) {
        for (HttpResponse response : responses) {
            assertEquals(code, response.statusCode());
        }
    }
}

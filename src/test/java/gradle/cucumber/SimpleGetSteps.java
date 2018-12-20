package gradle.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;
import java.net.http.HttpResponse;

public class SimpleGetSteps {
  private String port;
  private HttpResponse<String> response;

  @Given("The server is running on port {string}")
  public void theServerIsRunningOnPort(String port) {
    this.port = port;
    new Thread(new Support(port)).start();
  }

  @When("I send method {string} for {string} to host at the specified port")
  public void iSendToHostAtTheSpecifiedPort(String method, String location) throws IOException, InterruptedException {
    this.response = new Client().request(port, method, location);
  }

  @Then("I should receive a response with version {string}")
  public void iShouldReceive(String version) {
    assert version.equals(response.version().toString());
  }

  @Then("Status code {int}")
  public void statusCode(int code) {
    assert code == response.statusCode();
  }
}

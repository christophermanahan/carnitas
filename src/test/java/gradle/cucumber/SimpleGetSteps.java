package gradle.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;

public class SimpleGetSteps {
  private String port;
  private String response;

  @Given("The server is running on port {string}")
  public void theServerIsRunningOnPort(String port) {
    this.port = port;
  }

  @When("I send {string} to host at the specified port")
  public void iSendToHostAtTheSpecifiedPort(String data) throws IOException {
    new Thread(new Support(port)).start();
    this.response = new Request(port).send(data);
  }

  @Then("I should receive {string}")
  public void iShouldReceive(String simpleGETResponse) {
    assert simpleGETResponse.equals(response);
  }
}

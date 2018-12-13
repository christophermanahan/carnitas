package gradle.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;

public class EchoSteps {
  private String port;
  private Response response;

  @Given("The server is running on port {string}")
  public void theServerIsRunningOnPort(String port) {
    this.port = port;
  }

  @When("I send {string} to address {string} at the specified port")
  public void iSendToAddressAtTheSpecifiedPort(String data, String address) throws IOException {
    new Thread(new Support(port)).start();
    response = new Request(address, port).send(data);
  }

  @Then("I should receive {string}")
  public void iShouldReceive(String data) {assert(data == response.contains());
  }
}

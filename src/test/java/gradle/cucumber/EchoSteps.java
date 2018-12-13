package gradle.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;

public class EchoSteps {
  private Integer port;
  private Response response;

  @Given("The server is running on port {int}")
  public void the_server_is_running_on_port(Integer int1) {
    port = int1;
  }

  @When("I send {string} to address {string} at the specified port")
  public void i_send_to_address_at_the_specified_port(String string, String string2) throws IOException {
    response = new Request(string2, port).send(string);
  }

  @Then("I should receive {string}")
  public void i_should_receive(String string) {
    assert(string == response.contains());
  }
}

Feature: Simple GET
  The server should respond to simple GET requests

  Scenario: Simple GET request
    Given The server is running on port "33333"
    When I send "GET simple_get HTTP/1.1" to host at the specified port
    Then I should receive "HTTP/1.1 200 OK"

Feature: Simple GET
  The server should respond to simple GET requests

  Scenario: Simple GET request
    Given The server is running on port "33333"
    When I send method "GET" for "/simple_get" to host at the specified port
    Then I should receive a response with version "HTTP/1.1"
    And Status code 200

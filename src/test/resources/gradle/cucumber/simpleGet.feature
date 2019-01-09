Feature: Simple GET
  The server should respond to simple GET requests

  Scenario: Simple GET request
    Given The server is running on port "33333"
    When I send method "GET" for "/simple_get" to host at the specified port 1 time
    Then I should receive responses with version "HTTP_1_1"
    And Status codes 200

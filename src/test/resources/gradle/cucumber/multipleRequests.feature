Feature: Multiple Requests
  The server should respond to multiple requests

  Scenario: Multiple requests
    Given The server is running on port "33333"
    When I send method "GET" for "/simple_get" to host at the specified port 3 times
    Then I should receive responses with version "HTTP_1_1"
    And Status codes 200
    And Bodies ""

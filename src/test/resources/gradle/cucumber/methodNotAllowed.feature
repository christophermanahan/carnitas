Feature: Method Not Allowed
  The server should respond to requests to uri's with invalid methods

  Scenario: Method Not Allowed request
    Given The server is running on port "33333"
    When I send method "POST" for "/simple_get" to host at the specified port
    Then I should receive a response with version "HTTP_1_1"
    And Status code 405

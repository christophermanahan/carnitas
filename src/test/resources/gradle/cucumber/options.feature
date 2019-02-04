Feature: Options
  The server should respond to options requests

  Scenario: Options Request
    Given The server is running on port "33333"
    When I send method "OPTIONS" for "/simple_get" to host at the specified port
    Then I should receive a response with version "HTTP_1_1"
    And Status code 200
    And Header "Allow" with values "GET, HEAD, OPTIONS"

Feature: Simple POST
  The server should respond to simple POST requests

  Scenario: Simple POST request
    Given The server is running on port "33333"
    When I send method "POST" for "/simple_post" with body "hello world" to host at the specified port
    Then I should receive a response with version "HTTP/1.1"
    And Status code 200
    And Body "hello world"

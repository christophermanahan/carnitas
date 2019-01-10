Feature: Simple POST
  The server should respond to simple POST requests

  Scenario: Simple POST request
    Given The server is running on port "33333"
    When I send method "POST" for "/simple_post" with body "hello world" to host at the specified port 1 time
    Then I should receive responses with version "HTTP_1_1"
    And Status codes 201
    And Body "hello world"
Feature: Simple HEAD
  The server should respond to simple HEAD requests

  Scenario: Simple HEAD request
    Given The server is running on port "33333"
    When I send method "HEAD" for "/simple_get" to host at the specified port
    Then I should receive a response with version "HTTP/1.1"
    And Status code 200
    And Body ""

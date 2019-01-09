Feature: Simple HEAD
  The server should respond to simple HEAD requests

  Scenario: Simple HEAD request
    Given The server is running on port "33333"
    When I send method "HEAD" for "/simple_head" to host at the specified port 1 time
    Then I should receive responses with version "HTTP_1_1"
    And Status codes 200

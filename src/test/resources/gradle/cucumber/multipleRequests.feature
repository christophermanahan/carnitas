Feature: Multiple Requests
  The server should respond to multiple requests

  Scenario: Multiple requests
    Given The server is running on port "33333"
    When I send method "GET" for "/simple_get" to host at the specified port
    And I send method "POST" for "/simple_post" with body "hello world" to host at the specified port
    Then I should receive responses with version "HTTP_1_1"
    And The 1st response should have status code 200
    And The 2nd response should have status code 201
    And The 2nd response should have body "hello world"

Feature: Not Found
  The server should respond to requests for resources that do not exist

  Scenario: Request to resource that does not exist
    Given The server is running on port "33333"
    When I send method "GET" for "/does_not_exist" to host at the specified port
    Then I should receive a response with version "HTTP_1_1"
    And Status code 404

Feature: Redirect
  The server should redirect requests if configured

  Scenario: Redirect request
    Given The server is running on port "33333"
    When I send method "GET" for "/redirect" to host at the specified port
    Then I should receive a response with version "HTTP_1_1"
    And Status code 302
    And Header "Location" with value "/redirected"

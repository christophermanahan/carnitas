Feature: Echo
  The server should echo back data sent to it

  Scenario: Simple GET request
    Given The server is running on port "33333"
    When I send "data" to address "127.0.0.1" at the specified port
    Then I should receive "HTTP/1.1 200 OK"

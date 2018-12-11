Feature: Echo
  The server should echo back data sent to it

  Scenario: Send data to the server
    Given The server is running on port 33333
    When I send "data" to address "127.0.0.1" at the specified port
    Then I should receive "data"

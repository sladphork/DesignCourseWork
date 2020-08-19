Feature: Acceptance Tests for '/iam/token'
  Description: User attempts to check a token (not implemented yet)


  Scenario: The endpoint is not implemented (temporary)
    Given a user attempting to check a token
    When they call the endpoint
    Then they receive a Not Implemented Response
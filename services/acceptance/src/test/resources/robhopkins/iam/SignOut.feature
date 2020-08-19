Feature: Acceptance Tests for '/iam/signout'
  Description: User attempts to sign out using the IAM service.

  Scenario Outline: A user with valid token signs out
    Given A <username> with <secret> that has signed in
    When The user attempts to sign out
    Then User has signed out successfully

    Examples:
      | username | secret |
      | testp | password |
      | testr | password |
      | tests | password |

Feature: Acceptance Tests for '/iam/signin'
  Description: User attempts to sign in using the IAM service.

  Scenario Outline: Unauthorized user is able to sign in.
    Given A user with <username>
    When The user attempts sign in with valid <secret>
    Then The user has signed in and has a Token with <role>

  Examples:
    | username | secret | role |
    | testp | password | PROFESSOR |
    | testr | password | REGISTRAR |
    | tests | password | STUDENT |

  Scenario Outline: User attempts to sign in with invalid secret
    Given A user with <username>
    When The user attempts to signin with invalid secret
    Then The user does not have a Token.

  Examples:
    | username |
    | testp |
    | testr |
    | tests |

  Scenario Outline: An unknwon User attempts to sign in
    Given A user with <username>
    When The user attempts to signin
    Then The user does not have a Token.

    Examples:
      | username |
      | unknown_prof |
      | unknown_reg |
      | unknown_student |

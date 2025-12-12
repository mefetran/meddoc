Feature: Validate user credentials
  The system should validate email, password and optionally name fields.
  Valid data should produce Success, invalid data should produce Error.

  #  Basic valid case
  Scenario: Valid credentials return Success
    Given a ValidateUserCredentialsUseCase
    When I validate credentials with email "test@test.ru" and password "password" and name "Denis"
    Then the result should be Success

  #  Empty email
  Scenario: Empty email returns invalid email error
    Given a ValidateUserCredentialsUseCase
    When I validate credentials with email "" and password "password"
    Then the result should be Error "Invalid email"

  #  Invalid email formats (ValueSource equivalent)
  Scenario Outline: Invalid email formats produce error
    Given a ValidateUserCredentialsUseCase
    When I validate credentials with email "<email>" and password "password"
    Then the result should be Error "Invalid email"

    Examples:
      | email            |
      | plainaddress     |
      | missingatsign.com |
      | a@b              |
      | user@.com        |

  #  Boundary value analysis for password
  Scenario: Password below minimum length produces error
    Given a ValidateUserCredentialsUseCase
    And PASSWORD_MIN_LENGTH = 8 and PASSWORD_MAX_LENGTH = 64
    When I validate credentials with password of length PASSWORD_MIN_LENGTH - 1
    Then the result should be Error "Invalid password length"

  Scenario: Password at minimum length is valid
    Given a ValidateUserCredentialsUseCase
    And PASSWORD_MIN_LENGTH = 8
    When I validate credentials with password of length PASSWORD_MIN_LENGTH
    Then the result should be Success

  Scenario: Password at maximum length is valid
    Given a ValidateUserCredentialsUseCase
    When I validate credentials with password of length PASSWORD_MAX_LENGTH
    Then the result should be Success

  Scenario: Password above maximum length produces error
    Given a ValidateUserCredentialsUseCase
    And PASSWORD_MAX_LENGTH = 64
    When I validate credentials with password of length PASSWORD_MAX_LENGTH + 1
    Then the result should be Error "Invalid password length"

  #  MCDC conditions (isBlank || badFormat)
  Scenario: MCDC - blank email produces error
    Given a ValidateUserCredentialsUseCase
    When I validate credentials with email "" and password "password"
    Then the result should be Error

  Scenario: MCDC - invalid email produces error
    Given a ValidateUserCredentialsUseCase
    When I validate credentials with email "bademail" and password "password"
    Then the result should be Error

  Scenario: MCDC - valid email produces Success
    Given a ValidateUserCredentialsUseCase
    When I validate credentials with email "test@test.ru" and password "password"
    Then the result should be Success

  #  Assumptions equivalent
  Scenario: Assumptions example (simulated) - only run when environment is valid
    Given a ValidateUserCredentialsUseCase
    And environment property "runUserValidationTests" is not "false"
    When I validate credentials with email "test@test.ru" and password "password"
    Then the result should be Success

  #  Assert variety
  Scenario: Result should not be Error
    Given a ValidateUserCredentialsUseCase
    When I validate credentials with email "test@test.ru" and password "password"
    Then the result should not be Error

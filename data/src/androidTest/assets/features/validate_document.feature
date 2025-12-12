Feature: Validate document fields
  Document validation includes checking title, category and optional content key-value pairs.
  Valid data should return Success, invalid data should return Error.

  #  Blank title
  Scenario: Blank title returns error
    Given a ValidateDocumentUseCase
    When I validate a document with title "" and category "Laboratory"
    Then the document result should be Error "Title cannot be blank"

  #  Title length boundary BVA
  Scenario: Title at maximum allowed length is valid
    Given a ValidateDocumentUseCase
    When I validate a document with title of length DOCUMENT_TITLE_LENGTH and category "Laboratory"
    Then the document result should be Success

  Scenario: Title above maximum length produces error
    Given a ValidateDocumentUseCase
    And DOCUMENT_TITLE_LENGTH = 200
    When I validate a document with title of length DOCUMENT_TITLE_LENGTH + 1 and category "Laboratory"
    Then the result should contain error message "Title is too long"

  #  Null category returns error
  Scenario: Null category returns error
    Given a ValidateDocumentUseCase
    When I validate a document with title "some title" and null category
    Then the document result should be Error "Category is required"

  #  Content key-value validations
  Scenario: Valid content key-value map returns Success
    Given a ValidateDocumentUseCase
    When I validate a document with title "some title" and category "Laboratory" and content:
      | A | desc |
    Then the document result should be Success

  Scenario: Blank key in content produces error
    Given a ValidateDocumentUseCase
    When I validate a document with title "some title" and category "Laboratory" and content:
      | "" | desc |
    Then the result should contain error message "Content key cannot be blank"

  Scenario: Blank value in content produces error
    Given a ValidateDocumentUseCase
    When I validate a document with title "some title" and category "Laboratory" and content:
      | A | "" |
    Then the result should contain error message "Content value cannot be blank"

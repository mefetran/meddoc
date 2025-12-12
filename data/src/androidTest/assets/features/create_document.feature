Feature: Create document
  Creating a document should call the repository and return the created document.

  #  Basic Behavior
  Scenario: Create document calls repository and returns created document
    Given a mocked DocumentRepository that returns a created document
    And a CreateDocumentUseCase
    When I execute CreateDocumentUseCase with valid fields
    Then the result should be a successful document with the same values
    And repository.createDocument should be invoked exactly once

  #  Relaxed mock behavior
  Scenario: Relaxed mock repository should accept calls without stubbing
    Given a relaxed mocked DocumentRepository
    And a CreateDocumentUseCase wrapping the relaxed repository
    When I execute CreateDocumentUseCase with valid fields but without stubbing
    Then repository.createDocument should have been called

  #  Spy repository behavior
  Scenario: Spy repository should return correct created document
    Given a spy DocumentRepository
    And a CreateDocumentUseCase wrapping the spy repository
    When I execute CreateDocumentUseCase with required fields
    Then the spy repository should return success with matching title
    And spy repository createDocument should have been invoked with correct parameters

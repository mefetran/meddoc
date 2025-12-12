Feature: Create document
  As a registered user of the medical documents service,
  I want to create new documents with metadata and content,
  So that I can store and manage my personal medical information.

  Business Idea:
  The creation of a document should be reliable and guarantee
  that the provided metadata is stored and returned correctly.
  Invalid or missing data should be detected early by the system,
  preventing corrupted entries in the medical archive.

  # BACKGROUND
  Background:
    Given the user is authenticated in the system
    And document creation functionality is available

  # RULE
  Rule: A document must be created only when all required fields are valid
  - Mandatory: title, category
  - Optional: description, date, priority, content
  - Repository must be called exactly once per valid creation request

  # DECLARATIVE
    Scenario: Create document successfully returns created document
      Given a mocked DocumentRepository that returns a created document
      And a CreateDocumentUseCase
      When I execute CreateDocumentUseCase with valid fields
      Then the result should be a successful document with the same values
      And repository.createDocument should be invoked exactly once

  # IMPERATIVE
    Scenario: Create document with table-defined input fields using relaxed mock
      Given a relaxed mocked DocumentRepository
      And a CreateDocumentUseCase wrapping the relaxed repository
      When I create a document with the following fields:
        | title       | some title     |
        | description | (null)         |
        | date        | (null)         |
        | file        | (null)         |
        | localPath   | (null)         |
        | category    | Laboratory     |
        | priority    | 0              |
      Then repository.createDocument should have been called

  # DECLARATIVE
    Scenario: Spy repository generates correct created document
      Given a spy DocumentRepository
      And a CreateDocumentUseCase wrapping the spy repository
      When I execute CreateDocumentUseCase with the following valid attributes:
        | title       | some title     |
        | description | (null)         |
        | date        | (null)         |
        | file        | (null)         |
        | localPath   | (null)         |
        | category    | Laboratory     |
        | priority    | 0              |
      Then the spy repository should return success with matching title
      And spy repository createDocument should have been invoked with correct parameters

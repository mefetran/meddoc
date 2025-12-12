Feature: Delete all local documents

  Scenario: DeleteAllDocumentsLocalUseCase calls repository
    Given a mocked DocumentRepository
    And a DeleteAllDocumentsLocalUseCase
    When I execute DeleteAllDocumentsLocalUseCase
    Then repository.deleteDocumentsListLocal should be called once

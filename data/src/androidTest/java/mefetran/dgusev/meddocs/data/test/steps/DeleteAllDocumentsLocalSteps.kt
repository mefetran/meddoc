package mefetran.dgusev.meddocs.data.test.steps

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.DeleteAllDocumentsLocalUseCaseImpl
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteAllDocumentsLocalUseCase

class DeleteAllDocumentsLocalSteps {
    private lateinit var repository: DocumentRepository
    private lateinit var useCase: DeleteAllDocumentsLocalUseCase

    @Given("a mocked DocumentRepository")
    fun givenMockRepository() {
        repository = mockk(relaxed = true)
    }

    @And("a DeleteAllDocumentsLocalUseCase")
    fun andUseCase() {
        useCase = DeleteAllDocumentsLocalUseCaseImpl(repository)
    }

    @When("I execute DeleteAllDocumentsLocalUseCase")
    fun executeUseCase() = runTest {
        useCase.execute(Unit)
    }

    @Then("repository.deleteDocumentsListLocal should be called once")
    fun verifyCall() {
        coVerify(exactly = 1) { repository.deleteDocumentsListLocal() }
        confirmVerified(repository)
    }
}
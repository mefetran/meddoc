package mefetran.dgusev.meddocs.data.test.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.CreateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.CreateDocumentUseCase
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CreateDocumentSteps {

    private lateinit var repository: DocumentRepository
    private lateinit var useCase: CreateDocumentUseCase
    private var result: Result<Document>? = null


    private val DOCUMENT_ID = "1"
    private val DOCUMENT_TITLE = "some title"
    private val DOCUMENT_DESCRIPTION = "some description"
    private val DOCUMENT_DATE = "2024-01-01"
    private val DOCUMENT_PRIORITY = 0
    private val DOCUMENT_CATEGORY = Category.Laboratory
    private val DOCUMENT_CONTENT = mapOf<String, String>()

    private lateinit var params: CreateDocumentUseCase.Params

    @Given("a mocked DocumentRepository that returns a created document")
    fun givenMockRepositoryReturningSuccess() {
        repository = mockk()

        val created = Document(
            id = DOCUMENT_ID,
            title = DOCUMENT_TITLE,
            description = DOCUMENT_DESCRIPTION,
            date = DOCUMENT_DATE,
            localFilePath = "",
            file = "",
            category = DOCUMENT_CATEGORY,
            priority = DOCUMENT_PRIORITY,
            content = DOCUMENT_CONTENT,
            createdAt = "",
            updatedAt = "",
        )

        coEvery {
            repository.createDocument(any(), any(), any(), any(), any(), any(), any(), any())
        } returns flow { emit(Result.success(created)) }
    }

    @Given("a CreateDocumentUseCase")
    fun givenUseCase() {
        useCase = CreateDocumentUseCaseImpl(repository)
    }

    @Given("a relaxed mocked DocumentRepository")
    fun givenRelaxedMock() {
        repository = mockk(relaxed = true)
    }

    @Given("a CreateDocumentUseCase wrapping the relaxed repository")
    fun givenUseCaseWithRelaxed() {
        useCase = CreateDocumentUseCaseImpl(repository)
    }

    @Given("a spy DocumentRepository")
    fun givenSpyRepository() {
        repository = spyk(object : DocumentRepository {
            override suspend fun getDocuments() =
                flow { emit(Result.success(emptyList<Document>())) }

            override suspend fun createDocument(
                title: String,
                description: String?,
                date: String?,
                localFilePath: String?,
                file: String?,
                category: Category?,
                priority: Int?,
                content: Map<String, String>?
            ) = flow {
                emit(
                    Result.success(
                        Document(
                            DOCUMENT_ID,
                            title,
                            description ?: "",
                            date ?: "",
                            localFilePath ?: "",
                            file ?: "",
                            category ?: Category.Other,
                            priority ?: 0,
                            content ?: emptyMap(),
                            createdAt = "",
                            updatedAt = ""
                        )
                    )
                )
            }

            override suspend fun getDocumentById(id: String) =
                flow { emit(Result.failure<Document>(RuntimeException("not implemented"))) }

            override suspend fun updateDocumentById(
                id: String,
                title: String?,
                description: String?,
                date: String?,
                file: String?,
                category: Category?,
                priority: Int?,
                content: Map<String, String>?
            ) = flow { emit(Result.failure<Document>(RuntimeException("not implemented"))) }

            override suspend fun deleteDocumentById(id: String) = Result.success(1)
            override suspend fun saveDocumentsListLocal(documentsList: List<Document>) {}
            override suspend fun getDocumentsListOrNullLocal(): List<Document>? = null
            override suspend fun deleteDocumentsListLocal() {}
            override suspend fun saveDocumentLocal(document: Document) {}
            override suspend fun getDocumentOrNullLocal(documentId: String): Document? = null
            override suspend fun deleteDocumentLocal(documentId: String) {}
            override suspend fun observeDocuments() = flow { emit(emptyList<Document>()) }
            override suspend fun updateDocumentLocal(
                id: String,
                title: String?,
                description: String?,
                date: String?,
                localFilePath: String?,
                file: String?,
                category: Category?,
                priority: Int?,
                content: Map<String, String>?
            ) { }
        })
    }

    @Given("a CreateDocumentUseCase wrapping the spy repository")
    fun givenUseCaseWithSpy() {
        useCase = CreateDocumentUseCaseImpl(repository)
    }

    @When("I execute CreateDocumentUseCase with valid fields")
    fun whenExecuteUseCase() = runTest {
        params = CreateDocumentUseCase.Params(
            title = DOCUMENT_TITLE,
            description = DOCUMENT_DESCRIPTION,
            date = DOCUMENT_DATE,
            localFilePath = "",
            file = "",
            category = DOCUMENT_CATEGORY,
            priority = DOCUMENT_PRIORITY,
            content = DOCUMENT_CONTENT,
        )
        val resultFlow = useCase.execute(params)
        result = resultFlow.first()
    }


    @When("I execute CreateDocumentUseCase with required fields")
    fun whenExecuteUseCaseWithRequiredFields() = runTest {
        params = CreateDocumentUseCase.Params(
            title = DOCUMENT_TITLE,
            description = null,
            date = null,
            localFilePath = null,
            file = null,
            category = DOCUMENT_CATEGORY,
            priority = DOCUMENT_PRIORITY,
            content = DOCUMENT_CONTENT,
        )
        val resultFlow = useCase.execute(params)
        result = resultFlow.first()
    }

    @When("I execute CreateDocumentUseCase with valid fields but without stubbing")
    fun whenExecuteUseCaseRelaxed() = runTest {
        params = CreateDocumentUseCase.Params(
            title = DOCUMENT_TITLE,
            description = null,
            date = null,
            localFilePath = null,
            file = null,
            category = DOCUMENT_CATEGORY,
            priority = DOCUMENT_PRIORITY,
            content = DOCUMENT_CONTENT,
        )
        useCase.execute(params)
    }

    @Then("the result should be a successful document with the same values")
    fun assertSuccessAndDocumentMatches() {
        assertTrue(result!!.isSuccess)
        val doc = result!!.getOrNull()
        assertNotNull(doc)
        assertEquals(DOCUMENT_TITLE, doc.title)
        assertEquals(DOCUMENT_DESCRIPTION, doc.description)
        assertEquals(DOCUMENT_DATE, doc.date)
    }

    @Then("repository.createDocument should be invoked exactly once")
    fun verifyRepositoryCalledOnce() {
        coVerify(exactly = 1) {
            repository.createDocument(any(), any(), any(), any(), any(), any(), any(), any())
        }
    }

    @Then("repository.createDocument should have been called")
    fun verifyRelaxedCall() {
        coVerify {
            repository.createDocument(any(), any(), any(), any(), any(), any(), any(), any())
        }
    }

    @Then("the spy repository should return success with matching title")
    fun assertSpyReturn() {
        val doc = result!!.getOrNull()
        assertNotNull(doc)
        assertEquals(DOCUMENT_TITLE, doc.title)
    }

    @Then("spy repository createDocument should have been invoked with correct parameters")
    fun verifySpyParams() {
        coVerify {
            repository.createDocument(
                DOCUMENT_TITLE,
                null,
                null,
                null,
                null,
                DOCUMENT_CATEGORY,
                DOCUMENT_PRIORITY,
                DOCUMENT_CONTENT
            )
        }
    }
}

package mefetran.dgusev.meddocs.data.test.steps

import io.cucumber.datatable.DataTable
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

    private var userAuthenticated = false
    private var featureAvailable = false

    @Given("the user is authenticated in the system")
    fun userAuthenticated() {
        userAuthenticated = true
    }

    @Given("document creation functionality is available")
    fun creationAvailable() {
        featureAvailable = true
    }

    @Given("a mocked DocumentRepository that returns a created document")
    fun givenMockRepositoryReturningSuccess() {
        assertTrue(userAuthenticated)
        assertTrue(featureAvailable)

        repository = mockk()

        val created = Document(
            id = "1",
            title = "some title",
            description = "some description",
            date = "2024-01-01",
            localFilePath = "",
            file = "",
            category = Category.Laboratory,
            priority = 0,
            content = emptyMap(),
            createdAt = "",
            updatedAt = "",
        )

        coEvery {
            repository.createDocument(
                any(), any(), any(), any(), any(),
                any(), any(), any()
            )
        } returns flow { emit(Result.success(created)) }
    }

    @Given("a CreateDocumentUseCase")
    fun givenUseCase() {
        assertTrue(::repository.isInitialized)
        useCase = CreateDocumentUseCaseImpl(repository)
    }

    @Given("a relaxed mocked DocumentRepository")
    fun givenRelaxedRepository() {
        assertTrue(userAuthenticated)
        assertTrue(featureAvailable)
        repository = mockk(relaxed = true)
    }

    @Given("a CreateDocumentUseCase wrapping the relaxed repository")
    fun givenUseCaseWithRelaxed() {
        useCase = CreateDocumentUseCaseImpl(repository)
    }

    @Given("a spy DocumentRepository")
    fun givenSpyRepository() {
        assertTrue(userAuthenticated)
        assertTrue(featureAvailable)

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
                content: Map<String, String>?,
            ) = flow {
                emit(
                    Result.success(
                        Document(
                            id = "1",
                            title = title,
                            description = description ?: "",
                            date = date ?: "",
                            localFilePath = localFilePath ?: "",
                            file = file ?: "",
                            category = category ?: Category.Other,
                            priority = priority ?: 0,
                            content = content ?: emptyMap(),
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
    fun whenExecuteUseCaseValid() = runTest {
        val params = CreateDocumentUseCase.Params(
            title = "some title",
            description = "some description",
            date = "2024-01-01",
            localFilePath = "",
            file = "",
            category = Category.Laboratory,
            priority = 0,
            content = emptyMap(),
        )

        result = useCase.execute(params).first()
    }

    @When("I execute CreateDocumentUseCase with valid fields but without stubbing")
    fun whenExecuteRelaxed() = runTest {
        val params = CreateDocumentUseCase.Params(
            title = "some title",
            description = null,
            date = null,
            localFilePath = null,
            file = null,
            category = Category.Laboratory,
            priority = 0,
            content = emptyMap(),
        )
        useCase.execute(params)   // relaxed mock returns flow<Unit>
    }

    @When("I create a document with the following fields:")
    fun whenExecuteWithTable(table: DataTable) = runTest {
        val map = table.asMap(String::class.java, String::class.java)

        val params = CreateDocumentUseCase.Params(
            title = map["title"]!!,
            description = map["description"].nullIfDeclaredNull(),
            date = map["date"].nullIfDeclaredNull(),
            localFilePath = map["localPath"].nullIfDeclaredNull(),
            file = map["file"].nullIfDeclaredNull(),
            category = map["category"]?.let { Category.valueOf(it) },
            priority = map["priority"]?.toIntOrNull(),
            content = emptyMap(),
        )

         useCase.execute(params)
    }

    @When("I execute CreateDocumentUseCase with the following valid attributes:")
    fun whenExecuteSpyWithTable(table: DataTable) = runTest {
        val map = table.asMap(String::class.java, String::class.java)

        val params = CreateDocumentUseCase.Params(
            title = map["title"]!!,
            description = map["description"].nullIfDeclaredNull(),
            date = map["date"].nullIfDeclaredNull(),
            localFilePath = map["localPath"].nullIfDeclaredNull(),
            file = map["file"].nullIfDeclaredNull(),
            category = map["category"]?.let { Category.valueOf(it) },
            priority = map["priority"]?.toIntOrNull(),
            content = emptyMap(),
        )

        result = useCase.execute(params).first()
    }

    @Then("the result should be a successful document with the same values")
    fun assertSuccessReturnedDocument() {
        assertTrue(result!!.isSuccess)
        val doc = result!!.getOrNull()
        assertNotNull(doc)
        assertEquals("some title", doc.title)
        assertEquals("some description", doc.description)
    }

    @Then("repository.createDocument should be invoked exactly once")
    fun verifyCalledOnce() {
        coVerify(exactly = 1) {
            repository.createDocument(any(), any(), any(), any(), any(), any(), any(), any())
        }
    }

    @Then("repository.createDocument should have been called")
    fun verifyAnyCall() {
        coVerify {
            repository.createDocument(any(), any(), any(), any(), any(), any(), any(), any())
        }
    }

    @Then("the spy repository should return success with matching title")
    fun verifySpyReturnedCorrectData() {
        val doc = result!!.getOrNull()
        assertNotNull(doc)
        assertEquals("some title", doc.title)
    }

    @Then("spy repository createDocument should have been invoked with correct parameters")
    fun verifySpyParams() {
        coVerify {
            repository.createDocument(
                "some title",
                null,
                null,
                null,
                null,
                Category.Laboratory,
                0,
                emptyMap()
            )
        }
    }

    private fun String?.nullIfDeclaredNull(): String? =
        if (this == null) null else if (this == "(null)") null else this
}

package mefetran.dgusev.meddocs.data.test.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.model.DOCUMENT_TITLE_LENGTH
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidateDocumentSteps {

    private lateinit var useCase: ValidateDocumentUseCase
    private var result: ValidateResult? = null

    private var maxTitleLength: Int = DOCUMENT_TITLE_LENGTH

    @Given("a ValidateDocumentUseCase")
    fun initUseCase() {
        useCase = ValidateDocumentUseCaseImpl()
    }

    @Given("DOCUMENT_TITLE_LENGTH = {int}")
    fun setMaxLength(max: Int) {
        maxTitleLength = max
    }

    @When(
        "I validate a document with title {string} and category {string}"
    )
    fun validateWithTitleAndCategory(title: String, category: String) = runTest {
        val cat = Category.valueOf(category)
        result = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = title,
                category = cat
            )
        )
    }

    @When("I validate a document with title {string} and null category")
    fun validateWithNullCategory(title: String) = runTest {
        result = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = title,
                category = null
            )
        )
    }

    @When(
        "I validate a document with title of length DOCUMENT_TITLE_LENGTH and category {string}"
    )
    fun validateMaxLength(category: String) = runTest {
        val title = "x".repeat(maxTitleLength)
        val cat = Category.valueOf(category)

        result = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = title,
                category = cat
            )
        )
    }

    @When(
        "I validate a document with title of length DOCUMENT_TITLE_LENGTH + 1 and category {string}"
    )
    fun validateTooLong(category: String) = runTest {
        val title = "x".repeat(maxTitleLength + 1)
        val cat = Category.valueOf(category)

        result = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = title,
                category = cat
            )
        )
    }

    @When(
        "I validate a document with title {string} and category {string} and content:"
    )
    fun validateWithContent(title: String, category: String, gherkinMap: Map<String?, String?>) =
        runTest {
            val cat = Category.valueOf(category)

            val content = gherkinMap
                .mapKeys { entry ->
                    when (val key = entry.key) {
                        null -> ""
                        "\"\"" -> ""
                        "''" -> ""
                        else -> key
                    }
                }
                .mapValues { entry ->
                    when (val value = entry.value) {
                        null -> ""
                        "\"\"" -> ""
                        "''" -> ""
                        else -> value
                    }
                }

            result = useCase.execute(
                ValidateDocumentUseCase.Params(
                    title = title,
                    category = cat,
                    content = content
                )
            )
        }

    @Then("the document result should be Success")
    fun assertSuccess() {
        assertTrue(result is ValidateResult.Success)
    }

    @Then("the document result should be Error {string}")
    fun assertErrorExact(msg: String) {
        assertTrue(result is ValidateResult.Error)
        assertEquals(msg, (result as ValidateResult.Error).message)
    }

    @Then("the result should contain error message {string}")
    fun assertErrorContains(msg: String) {
        assertTrue(result is ValidateResult.Error)
        assertTrue((result as ValidateResult.Error).message.contains(msg))
    }
}

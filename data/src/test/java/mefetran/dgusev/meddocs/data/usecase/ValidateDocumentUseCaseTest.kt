package mefetran.dgusev.meddocs.data.usecase

import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.model.DOCUMENT_TITLE_LENGTH
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_CATEGORY_IS_REQUIRED
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_CONTENT_KEY_CANNOT_BE_BLANK
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_CONTENT_VALUE_CANNOT_BE_BLANK
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_TITLE_IS_TOO_LONG
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


private const val DOCUMENT_TITLE = "some title"
private val DOCUMENT_CATEGORY = Category.Laboratory

@DisplayName("Class that tests validation of documents")
class ValidateDocumentUseCaseTest {
    private lateinit var useCase: ValidateDocumentUseCase

    @BeforeEach
    fun setup() {
        useCase = ValidateDocumentUseCaseImpl()
    }

    @Test
    fun `blank title returns error`() = runTest {
        val params = ValidateDocumentUseCase.Params(
            title = "",
            category = DOCUMENT_CATEGORY
        )
        val result = useCase.execute(params)
        assertTrue(result is ValidateResult.Error)
        assertEquals("Title cannot be blank", (result as ValidateResult.Error).message)
    }

    @Test
    fun `title length boundary - BVA`() = runTest {
        val maxLength = DOCUMENT_TITLE_LENGTH
        val okTitle = "x".repeat(maxLength)
        val tooLong = "x".repeat(maxLength + 1)

        val resultOk = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = okTitle,
                category = DOCUMENT_CATEGORY
            )
        )
        assertTrue(resultOk is ValidateResult.Success)

        val resultTooLong = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = tooLong,
                category = DOCUMENT_CATEGORY
            )
        )
        assertTrue(resultTooLong is ValidateResult.Error)
        assertThat(
            (resultTooLong as ValidateResult.Error).message,
            containsString(ERROR_TITLE_IS_TOO_LONG)
        )
    }

    @Test
    fun `null category returns error`() = runTest {
        val result =
            useCase.execute(ValidateDocumentUseCase.Params(title = DOCUMENT_TITLE, category = null))
        assertTrue(result is ValidateResult.Error)
        assertEquals(ERROR_CATEGORY_IS_REQUIRED, (result as ValidateResult.Error).message)
    }

    @Test
    fun `content key-value validations`() = runTest {
        val goodContent = mapOf("A" to "desc")
        val badKeyBlank = mapOf("" to "desc")
        val badValueBlank = mapOf("A" to "")

        val resultGood = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = DOCUMENT_TITLE,
                category = DOCUMENT_CATEGORY,
                content = goodContent
            )
        )
        assertTrue(resultGood is ValidateResult.Success)

        val resultKeyBlank = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = DOCUMENT_TITLE,
                category = DOCUMENT_CATEGORY,
                content = badKeyBlank
            )
        )
        assertTrue(resultKeyBlank is ValidateResult.Error)
        assertThat(
            (resultKeyBlank as ValidateResult.Error).message,
            containsString(ERROR_CONTENT_KEY_CANNOT_BE_BLANK)
        )

        val resultValueBlank = useCase.execute(
            ValidateDocumentUseCase.Params(
                title = DOCUMENT_TITLE,
                category = DOCUMENT_CATEGORY,
                content = badValueBlank
            )
        )
        assertTrue(resultValueBlank is ValidateResult.Error)
        assertThat(
            (resultValueBlank as ValidateResult.Error).message,
            containsString(ERROR_CONTENT_VALUE_CANNOT_BE_BLANK)
        )
    }
}
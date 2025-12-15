package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.model.DOCUMENT_CONTENT_ITEM_DESC_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_CONTENT_ITEM_TITLE_LENGTH
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase

class ValidateDocumentContentFuzzTest {
    companion object {
        private val useCase = ValidateDocumentUseCaseImpl()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val title = "Valid Title"
            val category = Category.Laboratory

            val contentSize = data.consumeInt(1, 10)
            val content = buildMap {
                repeat(contentSize) {
                    val keyLength = data.consumeInt(0, DOCUMENT_CONTENT_ITEM_TITLE_LENGTH + 50)
                    val valueLength = data.consumeInt(0, DOCUMENT_CONTENT_ITEM_DESC_LENGTH + 50)

                    val key = data.consumeString(keyLength)
                    val value = data.consumeString(valueLength)

                    put(key, value)
                }
            }

            runBlocking {
                val result = useCase.execute(
                    ValidateDocumentUseCase.Params(
                        title = title,
                        category = category,
                        content = content
                    )
                )

                val hasBlankKey = content.keys.any { it.isBlank() }
                val hasTooLongKey = content.keys.any { it.length > DOCUMENT_CONTENT_ITEM_TITLE_LENGTH }
                val hasBlankValue = content.values.any { it.isBlank() }
                val hasTooLongValue = content.values.any { it.length > DOCUMENT_CONTENT_ITEM_DESC_LENGTH }


                val isInvalid =
                    hasBlankKey || hasTooLongKey || hasBlankValue || hasTooLongValue

                if (isInvalid) {
                    if (result !is ValidateResult.Error) {
                        throw AssertionError("Invalid content should give Error, got: $result")
                    }
                } else {
                    if (result !is ValidateResult.Success) {
                        throw AssertionError("Valid content should give Success, got: $result")
                    }
                }
            }
        }
    }
}

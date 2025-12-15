package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.model.DOCUMENT_TITLE_LENGTH
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_TITLE_CANNOT_BE_BLANK
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_TITLE_IS_TOO_LONG
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase


class ValidateDocumentTitleFuzzTest {
    companion object {
        private val useCase = ValidateDocumentUseCaseImpl()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val titleLength = data.consumeInt(0, DOCUMENT_TITLE_LENGTH + 100)
            val title = data.consumeString(titleLength)

            val category = Category.Laboratory

            runBlocking {
                val result = useCase.execute(
                    ValidateDocumentUseCase.Params(
                        title = title,
                        category = category
                    )
                )

                when {
                    title.isBlank() -> {
                        if (result !is ValidateResult.Error || result.message != ERROR_TITLE_CANNOT_BE_BLANK) {
                            throw AssertionError(
                                "Blank title should give ERROR_TITLE_CANNOT_BE_BLANK, got: $result"
                            )
                        }
                    }
                    title.length > DOCUMENT_TITLE_LENGTH -> {
                        if (result !is ValidateResult.Error || result.message != ERROR_TITLE_IS_TOO_LONG) {
                            throw AssertionError(
                                "Too long title (${title.length}) should give ERROR_TITLE_IS_TOO_LONG, got: $result"
                            )
                        }
                    }
                    else -> {
                        if (result !is ValidateResult.Success) {
                            throw AssertionError(
                                "Valid title should give Success, got: $result"
                            )
                        }
                    }
                }
            }
        }
    }
}

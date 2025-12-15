package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_PRIORITY_MUST_BE_GREAT_THEN_OR_EQUAL_ZERO
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase

class ValidateDocumentPriorityFuzzTest {
    companion object {
        private val useCase = ValidateDocumentUseCaseImpl()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val title = "Valid Title"
            val category = Category.Laboratory

            val priority = if (data.consumeBoolean()) {
                data.consumeInt(Int.MIN_VALUE, Int.MAX_VALUE)
            } else {
                null
            }

            runBlocking {
                val result = useCase.execute(
                    ValidateDocumentUseCase.Params(
                        title = title,
                        category = category,
                        priority = priority
                    )
                )

                if (priority != null && priority < 0) {
                    if (result !is ValidateResult.Error || result.message != ERROR_PRIORITY_MUST_BE_GREAT_THEN_OR_EQUAL_ZERO) {
                        throw AssertionError(
                            "Negative priority $priority should give ERROR_PRIORITY_MUST_BE_GREAT_THEN_OR_EQUAL_ZERO, got: $result"
                        )
                    }
                } else {
                    if (result !is ValidateResult.Success) {
                        throw AssertionError(
                            "Valid priority $priority should give Success, got: $result"
                        )
                    }
                }
            }
        }
    }
}

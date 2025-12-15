package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_CATEGORY_IS_REQUIRED
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase

class ValidateDocumentCategoryFuzzTest {
    companion object {
        private val useCase = ValidateDocumentUseCaseImpl()
        private val categories = Category.values()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val title = "Valid Title"

            val category = if (data.consumeBoolean()) {
                null
            } else {
                data.pickValue(categories)
            }

            runBlocking {
                val result = useCase.execute(
                    ValidateDocumentUseCase.Params(
                        title = title,
                        category = category
                    )
                )

                if (category == null) {
                    if (result !is ValidateResult.Error || result.message != ERROR_CATEGORY_IS_REQUIRED) {
                        throw AssertionError(
                            "Null category should give ERROR_CATEGORY_IS_REQUIRED, got: $result"
                        )
                    }
                } else {
                    if (result !is ValidateResult.Success) {
                        throw AssertionError(
                            "Valid category $category should give Success, got: $result"
                        )
                    }
                }
            }
        }
    }
}

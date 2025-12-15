package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase

class ValidateDocumentNoCrashFuzzTest {
    companion object {
        private val useCase = ValidateDocumentUseCaseImpl()
        private val categories = Category.values()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val title = data.consumeString(1000)
            val description = if (data.consumeBoolean()) data.consumeString(1000) else null
            val category = if (data.consumeBoolean()) data.pickValue(categories) else null
            val priority = if (data.consumeBoolean()) data.consumeInt() else null
            val date = if (data.consumeBoolean()) data.consumeString(50) else null

            val content = if (data.consumeBoolean()) {
                val size = data.consumeInt(0, 20)
                buildMap<String, String> {
                    repeat(size) {
                        val key = data.consumeString(200)
                        val value = data.consumeString(1000)
                        put(key, value)
                    }
                }
            } else null

            runBlocking {
                try {
                    useCase.execute(
                        ValidateDocumentUseCase.Params(
                            title = title,
                            description = description,
                            category = category,
                            priority = priority,
                            content = content,
                            date = date
                        )
                    )
                } catch (e: Throwable) {
                    throw AssertionError(
                        "Document validation crashed on: title='$title', category=$category",
                        e
                    )
                }
            }
        }
    }
}

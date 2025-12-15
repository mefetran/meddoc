package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.model.DOCUMENT_CONTENT_ITEM_DESC_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_CONTENT_ITEM_TITLE_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_DESCRIPTION_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_TITLE_LENGTH
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase

class ValidateDocumentInvariantsFuzzTest {
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
                val result = useCase.execute(
                    ValidateDocumentUseCase.Params(
                        title = title,
                        description = description,
                        category = category,
                        priority = priority,
                        content = content,
                        date = date
                    )
                )

                if (result is ValidateResult.Success) {
                    if (title.isBlank()) {
                        throw AssertionError("Success with blank title")
                    }

                    if (title.length > DOCUMENT_TITLE_LENGTH) {
                        throw AssertionError("Success with too long title: ${title.length}")
                    }

                    description?.let {
                        if (it.length > DOCUMENT_DESCRIPTION_LENGTH) {
                            throw AssertionError("Success with too long description: ${it.length}")
                        }
                    }

                    if (category == null) {
                        throw AssertionError("Success without category")
                    }

                    priority?.let {
                        if (it < 0) {
                            throw AssertionError("Success with negative priority: $it")
                        }
                    }

                    content?.forEach { (key, value) ->
                        if (key.isBlank()) {
                            throw AssertionError("Success with blank content key")
                        }
                        if (key.length > DOCUMENT_CONTENT_ITEM_TITLE_LENGTH) {
                            throw AssertionError("Success with too long content key: ${key.length}")
                        }
                        if (value.isBlank()) {
                            throw AssertionError("Success with blank content value for key: $key")
                        }
                        if (value.length > DOCUMENT_CONTENT_ITEM_DESC_LENGTH) {
                            throw AssertionError("Success with too long content value: ${value.length}")
                        }
                    }

                    date?.let {
                        val regex = Regex("""\d{4}-\d{2}-\d{2}""")
                        if (!regex.matches(it)) {
                            throw AssertionError("Success with invalid date format: $it")
                        }
                    }
                }
            }
        }
    }
}

package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.usecase.document.ERROR_DATE_MUST_BE_IN_FORMAT
import mefetran.dgusev.meddocs.data.usecase.document.ValidateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase

class ValidateDocumentDateFormatFuzzTest {
    companion object {
        private val useCase = ValidateDocumentUseCaseImpl()
        private val dateRegex = Regex("""\d{4}-\d{2}-\d{2}""")

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val title = "Valid Title"
            val category = Category.Laboratory

            val date = when (data.consumeInt(0, 5)) {
                0 -> null
                1 -> {
                    val year = data.consumeInt(1900, 2100)
                    val month = data.consumeInt(1, 12)
                    val day = data.consumeInt(1, 28)
                    "%04d-%02d-%02d".format(year, month, day)
                }
                2 -> {
                    data.consumeString(50)
                }
                3 -> {
                    "${data.consumeInt(0, 999)}-${data.consumeInt(0, 99)}-${data.consumeInt(0, 99)}"
                }
                4 -> {
                    val year = data.consumeInt(1900, 2100)
                    val month = data.consumeInt(1, 12)
                    val day = data.consumeInt(1, 28)
                    "$year/${month}/${day}"
                }
                else -> {
                    buildString {
                        repeat(data.consumeInt(5, 20)) {
                            if (data.consumeBoolean()) {
                                append(data.consumeInt(0, 9))
                            } else {
                                append(data.consumeChar())
                            }
                        }
                    }
                }
            }

            runBlocking {
                val result = useCase.execute(
                    ValidateDocumentUseCase.Params(
                        title = title,
                        category = category,
                        date = date
                    )
                )

                if (date != null) {
                    val isValidFormat = dateRegex.matches(date)

                    if (!isValidFormat) {
                        if (result !is ValidateResult.Error || result.message != ERROR_DATE_MUST_BE_IN_FORMAT) {
                            throw AssertionError(
                                "Invalid date format '$date' should give ERROR_DATE_MUST_BE_IN_FORMAT, got: $result"
                            )
                        }
                    } else {
                        if (result !is ValidateResult.Success) {
                            throw AssertionError(
                                "Valid date '$date' should give Success, got: $result"
                            )
                        }
                    }
                } else {
                    if (result !is ValidateResult.Success) {
                        throw AssertionError(
                            "No date should give Success, got: $result"
                        )
                    }
                }
            }
        }
    }
}

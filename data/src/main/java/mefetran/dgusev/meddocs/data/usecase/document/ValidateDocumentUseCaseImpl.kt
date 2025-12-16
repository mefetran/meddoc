package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.data.model.DOCUMENT_CONTENT_ITEM_DESC_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_CONTENT_ITEM_TITLE_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_DESCRIPTION_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_TITLE_LENGTH
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase
import javax.inject.Inject

const val ERROR_TITLE_CANNOT_BE_BLANK = "Title cannot be blank"
const val ERROR_TITLE_IS_TOO_LONG = "Title is too long"
const val ERROR_DESCRIPTION_IS_TOO_LONG = "Description is too long"
const val ERROR_CATEGORY_IS_REQUIRED = "Category is required"
const val ERROR_PRIORITY_MUST_BE_GREAT_THEN_OR_EQUAL_ZERO = "Priority must be >= 0"
const val ERROR_CONTENT_KEY_IS_TOO_LONG = "Content key is too long"
const val ERROR_CONTENT_KEY_CANNOT_BE_BLANK = "Content key cannot be blank"
const val ERROR_CONTENT_VALUE_IS_TOO_LONG = "Content value is too long"
const val ERROR_CONTENT_VALUE_CANNOT_BE_BLANK = "Content value cannot be blank"
const val ERROR_DATE_MUST_BE_IN_FORMAT = "Date must be in format YYYY-MM-DD"

class ValidateDocumentUseCaseImpl @Inject constructor() : ValidateDocumentUseCase {

    override suspend fun execute(input: ValidateDocumentUseCase.Params): ValidateResult {

//      for fuzzing
//        val year = input.date!![0].digitToInt()

        if (input.title.isBlank()) {
            return ValidateResult.Error(ERROR_TITLE_CANNOT_BE_BLANK)
        }

        if (input.title.length > DOCUMENT_TITLE_LENGTH) {
            return ValidateResult.Error(ERROR_TITLE_IS_TOO_LONG)
        }

        input.description?.let {
            if (it.length > DOCUMENT_DESCRIPTION_LENGTH)
                return ValidateResult.Error(ERROR_DESCRIPTION_IS_TOO_LONG)
        }

        input.category ?: return ValidateResult.Error(ERROR_CATEGORY_IS_REQUIRED)

        input.priority?.let {
            if (it < 0) return ValidateResult.Error(ERROR_PRIORITY_MUST_BE_GREAT_THEN_OR_EQUAL_ZERO)
        }

        input.content?.forEach { (key, value) ->
            if (key.length > DOCUMENT_CONTENT_ITEM_TITLE_LENGTH)
                return ValidateResult.Error("$ERROR_CONTENT_KEY_IS_TOO_LONG: $key")

            if (key.isBlank())
                return ValidateResult.Error(ERROR_CONTENT_KEY_CANNOT_BE_BLANK)

            if (value.length > DOCUMENT_CONTENT_ITEM_DESC_LENGTH)
                return ValidateResult.Error(ERROR_CONTENT_VALUE_IS_TOO_LONG)

            if (value.isBlank())
                return ValidateResult.Error(ERROR_CONTENT_VALUE_CANNOT_BE_BLANK)
        }

        input.date?.let { date ->
            val regex = Regex("""\d{4}-\d{2}-\d{2}""")
            if (!regex.matches(date)) {
                return ValidateResult.Error(ERROR_DATE_MUST_BE_IN_FORMAT)
            }
        }

        return ValidateResult.Success
    }
}

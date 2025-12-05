package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.data.model.DOCUMENT_CONTENT_ITEM_DESC_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_CONTENT_ITEM_TITLE_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_DESCRIPTION_LENGTH
import mefetran.dgusev.meddocs.data.model.DOCUMENT_TITLE_LENGTH
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.document.ValidateDocumentUseCase
import javax.inject.Inject

class ValidateDocumentUseCaseImpl @Inject constructor() : ValidateDocumentUseCase {

    override suspend fun execute(input: ValidateDocumentUseCase.Params): ValidateResult {
        if (input.title.isBlank()) {
            return ValidateResult.Error("Title cannot be blank")
        }

        if (input.title.length > DOCUMENT_TITLE_LENGTH) {
            return ValidateResult.Error("Title is too long")
        }

        input.description?.let {
            if (it.length > DOCUMENT_DESCRIPTION_LENGTH)
                return ValidateResult.Error("Description is too long")
        }

        input.category ?: return ValidateResult.Error("Category is required")

        input.priority?.let {
            if (it < 0) return ValidateResult.Error("Priority must be >= 0")
        }

        input.content?.forEach { (key, value) ->
            if (key.length > DOCUMENT_CONTENT_ITEM_TITLE_LENGTH)
                return ValidateResult.Error("Content key too long: $key")

            if (key.isBlank())
                return ValidateResult.Error("Content key cannot be blank")

            if (value.length > DOCUMENT_CONTENT_ITEM_DESC_LENGTH)
                return ValidateResult.Error("Content value too long")

            if (value.isBlank())
                return ValidateResult.Error("Content value cannot be blank")
        }

        input.date?.let { date ->
            val regex = Regex("""\d{4}-\d{2}-\d{2}""")
            if (!regex.matches(date)) {
                return ValidateResult.Error("Date must be in format YYYY-MM-DD")
            }
        }

        return ValidateResult.Success
    }
}

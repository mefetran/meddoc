package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.UpdateDocumentLocalUseCase
import javax.inject.Inject

class UpdateDocumentLocalUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : UpdateDocumentLocalUseCase {
    override suspend fun execute(input: UpdateDocumentLocalUseCase.Params) {
        documentRepository.updateDocumentLocal(
            id = input.id,
            title = input.title,
            description = input.description,
            date = input.date,
            localFilePath = input.localFilePath,
            file = input.file,
            category = input.category,
            priority = input.priority,
            content = input.content,
        )
    }
}
package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteDocumentLocalUseCase
import javax.inject.Inject

class DeleteDocumentLocalUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : DeleteDocumentLocalUseCase {
    override suspend fun execute(input: DeleteDocumentLocalUseCase.Params) {
        documentRepository.deleteDocumentLocal(input.documentId)
    }
}

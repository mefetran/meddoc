package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteDocumentRemoteUseCase
import javax.inject.Inject

class DeleteDocumentRemoteUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : DeleteDocumentRemoteUseCase {
    override suspend fun execute(input: DeleteDocumentRemoteUseCase.Params): Result<Int> = documentRepository.deleteDocumentById(input.documentId)
}

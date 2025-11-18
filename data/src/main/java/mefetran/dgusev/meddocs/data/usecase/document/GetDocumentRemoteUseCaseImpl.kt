package mefetran.dgusev.meddocs.data.usecase.document

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentRemoteUseCase
import javax.inject.Inject

class GetDocumentRemoteUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : GetDocumentRemoteUseCase {
    override suspend fun execute(input: GetDocumentRemoteUseCase.Params): Flow<Result<Document>> = documentRepository.getDocumentById(input.documentId)
}

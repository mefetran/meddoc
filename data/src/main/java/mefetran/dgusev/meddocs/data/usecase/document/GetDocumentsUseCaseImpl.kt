package mefetran.dgusev.meddocs.data.usecase.document

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentsUseCase
import javax.inject.Inject

class GetDocumentsUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : GetDocumentsUseCase {
    override suspend fun execute(input: Unit): Flow<Result<List<Document>>> = documentRepository.getDocuments()
}

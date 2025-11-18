package mefetran.dgusev.meddocs.data.usecase.document

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.ObserveDocumentsUseCase
import javax.inject.Inject

class ObserveDocumentsUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : ObserveDocumentsUseCase {
    override suspend fun execute(input: Unit): Flow<List<Document>> = documentRepository.observeDocuments()
}

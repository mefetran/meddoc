package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentLocalUseCase
import javax.inject.Inject

class GetDocumentLocalUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : GetDocumentLocalUseCase {
    override suspend fun execute(input: GetDocumentLocalUseCase.Params): Document? = documentRepository.getDocumentOrNullLocal(input.documentId)
}

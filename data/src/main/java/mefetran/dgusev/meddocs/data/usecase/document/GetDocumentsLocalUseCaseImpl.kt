package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentsLocalUseCase
import javax.inject.Inject

class GetDocumentsLocalUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : GetDocumentsLocalUseCase {
    override suspend fun execute(input: Unit): List<Document>? = documentRepository.getDocumentsListOrNullLocal()
}
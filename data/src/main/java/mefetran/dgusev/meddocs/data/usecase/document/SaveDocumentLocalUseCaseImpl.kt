package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.SaveDocumentLocalUseCase
import javax.inject.Inject

class SaveDocumentLocalUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : SaveDocumentLocalUseCase {
    override suspend fun execute(input: SaveDocumentLocalUseCase.Params) {
        documentRepository.saveDocumentLocal(input.document)
    }
}

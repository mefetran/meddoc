package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.SaveDocumentsListLocalUseCase
import javax.inject.Inject

class SaveDocumentsListLocalUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : SaveDocumentsListLocalUseCase {
    override suspend fun execute(input: SaveDocumentsListLocalUseCase.Params) {
        documentRepository.saveDocumentsListLocal(input.documents)
    }
}

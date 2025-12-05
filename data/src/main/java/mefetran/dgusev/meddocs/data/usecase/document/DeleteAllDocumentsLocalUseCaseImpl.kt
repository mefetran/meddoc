package mefetran.dgusev.meddocs.data.usecase.document

import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteAllDocumentsLocalUseCase
import javax.inject.Inject

class DeleteAllDocumentsLocalUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
)  : DeleteAllDocumentsLocalUseCase {
    override suspend fun execute(input: Unit) {
        documentRepository.deleteDocumentsListLocal()
    }
}

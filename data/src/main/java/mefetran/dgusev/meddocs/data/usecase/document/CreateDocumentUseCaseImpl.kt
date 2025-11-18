package mefetran.dgusev.meddocs.data.usecase.document

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.CreateDocumentUseCase
import javax.inject.Inject

class CreateDocumentUseCaseImpl @Inject constructor(
    private val documentRepository: DocumentRepository,
) : CreateDocumentUseCase {
    override suspend fun execute(input: CreateDocumentUseCase.Params): Flow<Result<Document>> = documentRepository.createDocument(
        title = input.title,
        description = input.description,
        date = input.date,
        file = input.file,
        category = input.category,
        priority = input.priority,
        content = input.content?.toMap()
    )
}

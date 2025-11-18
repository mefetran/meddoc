package mefetran.dgusev.meddocs.domain.usecase.document

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface GetDocumentsUseCase : UseCase<Unit, Flow<Result<List<Document>>>>

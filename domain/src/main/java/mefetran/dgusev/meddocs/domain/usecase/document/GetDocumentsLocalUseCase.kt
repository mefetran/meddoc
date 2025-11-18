package mefetran.dgusev.meddocs.domain.usecase.document

import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface GetDocumentsLocalUseCase : UseCase<Unit, List<Document>?>
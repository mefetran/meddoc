package mefetran.dgusev.meddocs.ui.screen.documentopen.model

import mefetran.dgusev.meddocs.data.model.Document

data class OpenDocumentState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val document: Document = Document(),
)
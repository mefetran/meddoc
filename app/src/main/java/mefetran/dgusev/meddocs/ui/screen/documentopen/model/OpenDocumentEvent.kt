package mefetran.dgusev.meddocs.ui.screen.documentopen.model

sealed interface OpenDocumentEvent {
    data object CloseScreen : OpenDocumentEvent
}
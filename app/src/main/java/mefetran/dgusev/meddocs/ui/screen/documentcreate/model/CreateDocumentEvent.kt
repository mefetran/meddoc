package mefetran.dgusev.meddocs.ui.screen.documentcreate.model

sealed interface CreateDocumentEvent {
    data object Back : CreateDocumentEvent

    data class ShowSnackbar(
        val message: String,
    ) : CreateDocumentEvent
}

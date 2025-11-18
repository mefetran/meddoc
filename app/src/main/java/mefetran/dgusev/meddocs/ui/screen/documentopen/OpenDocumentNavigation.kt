package mefetran.dgusev.meddocs.ui.screen.documentopen

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.app.saveEncryptedPdf
import mefetran.dgusev.meddocs.ui.components.ObserveAsEvents
import mefetran.dgusev.meddocs.ui.screen.documentopen.model.OpenDocumentEvent

@Serializable
internal data class OpenDocument(val documentId: String)

fun NavGraphBuilder.openDocumentDestination(
    onBackClick: () -> Unit,
) {
    composable<OpenDocument> { navEntry ->
        val openDocumentViewModel = hiltViewModel<OpenDocumentViewModel>()
        val state by openDocumentViewModel.state.collectAsStateWithLifecycle()
        val documentId = navEntry.toRoute<OpenDocument>().documentId
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            openDocumentViewModel.loadDocument(documentId)
        }

        ObserveAsEvents(flow = openDocumentViewModel.uiEvent) { event ->
            when(event) {
                OpenDocumentEvent.CloseScreen -> onBackClick()
            }
        }

        OpenDocumentScreen(
            state = state,
            onBackClick = onBackClick,
            onDeleteClick = {
                openDocumentViewModel.deleteDocument(state.document.id)
            },
            onPdfFileSelected = { uri ->
                val file = saveEncryptedPdf(
                    context = context,
                    uri = uri,
                )

                openDocumentViewModel.saveLocalFilePath(file = file)
            }
        )
    }
}

fun NavController.navigateToOpenDocument(documentId: String) {
    navigate(OpenDocument(documentId))
}
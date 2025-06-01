package mefetran.dgusev.meddocs.ui.screen.documentopen

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenDocument(val documentId: String)

fun NavGraphBuilder.openDocumentDestination(
    onBackClick: () -> Unit,
) {
    composable<OpenDocument> { navEntry ->
        val openDocumentViewModel = hiltViewModel<OpenDocumentViewModel>()
        val state by openDocumentViewModel.state.collectAsStateWithLifecycle()
        val documentId = navEntry.toRoute<OpenDocument>().documentId

        LaunchedEffect(Unit) {
            openDocumentViewModel.loadDocument(documentId)
        }

        OpenDocumentScreen(
            state = state,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToOpenDocument(documentId: String) {
    navigate(OpenDocument(documentId))
}
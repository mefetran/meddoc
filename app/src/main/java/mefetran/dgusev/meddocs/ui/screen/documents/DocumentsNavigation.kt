package mefetran.dgusev.meddocs.ui.screen.documents

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
internal data object Documents

fun NavGraphBuilder.documentsDestination(
    onNavigateToCreateDocument: () -> Unit,
) {
    composable<Documents> {
        val documentsViewModel = hiltViewModel<DocumentsViewModel>()
        val state by documentsViewModel.state.collectAsState()

        DocumentsScreen(
            state = state,
            onNavigateToCreateDocument = onNavigateToCreateDocument,
        )
    }
}
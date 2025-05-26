package mefetran.dgusev.meddocs.ui.screen.documents

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
internal data object Documents

fun NavGraphBuilder.documentsDestination(
    onNavigateToCreateDocument: () -> Unit,
) {
    composable<Documents> {
        DocumentsScreen(
            onNavigateToCreateDocument = onNavigateToCreateDocument,
        )
    }
}
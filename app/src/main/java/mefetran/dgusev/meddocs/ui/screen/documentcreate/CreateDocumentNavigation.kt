package mefetran.dgusev.meddocs.ui.screen.documentcreate

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.ui.components.ObserveAsEvents
import mefetran.dgusev.meddocs.ui.screen.documentcreate.model.CreateDocumentEvent

@Serializable
internal data object CreateDocument

fun NavGraphBuilder.createDocumentDestination(
    onBackClick: () -> Unit,
) {
    composable<CreateDocument> {
        val createDocumentViewModel = hiltViewModel<CreateDocumentViewModel>()
        val state by createDocumentViewModel.state.collectAsStateWithLifecycle()
        val title by createDocumentViewModel.documentTitle.collectAsStateWithLifecycle()
        val description by createDocumentViewModel.documentDescription.collectAsStateWithLifecycle()
        val date by createDocumentViewModel.date.collectAsStateWithLifecycle()
        val newField by createDocumentViewModel.contentItemTitle.collectAsStateWithLifecycle()
        val newFieldValue by createDocumentViewModel.contentItemDescription.collectAsStateWithLifecycle()

        ObserveAsEvents(flow = createDocumentViewModel.uiEvents) { event ->
            when (event) {
                CreateDocumentEvent.Back -> onBackClick()
            }
        }

        CreateDocumentScreen(
            title = title,
            description = description,
            date = date,
            category = state.category,
            contentMap = state.contentMap,
            newField = newField,
            newFieldValue = newFieldValue,
            onCreateDocument = createDocumentViewModel::createDocument,
            onTitleChange = createDocumentViewModel::changeDocumentTitle,
            onDescriptionChange = createDocumentViewModel::changeDocumentDescription,
            onDateChange = createDocumentViewModel::changeDate,
            onCategoryChange = createDocumentViewModel::changeCategory,
            onNewFieldChange = createDocumentViewModel::changeContentItemTitle,
            onNewFieldValueChange = createDocumentViewModel::changeContentItemDescription,
            onBackClick = onBackClick,
            onAddContentClick = createDocumentViewModel::addContentItem,
            onDeleteContentItemClick = createDocumentViewModel::deleteContentItem,
        )
    }
}

fun NavController.navigateToCreateDocument() {
    navigate(CreateDocument)
}
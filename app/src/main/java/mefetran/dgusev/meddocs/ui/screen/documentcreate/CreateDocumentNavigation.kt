package mefetran.dgusev.meddocs.ui.screen.documentcreate

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.ui.screen.documentcreate.model.CreateDocumentEvent

@Serializable
internal data object CreateDocument

fun NavGraphBuilder.createDocumentDestination(
    onBackClick: () -> Unit,
) {
    composable<CreateDocument> {
        val createDocumentViewModel = hiltViewModel<CreateDocumentViewModel>()
        val state by createDocumentViewModel.state.collectAsStateWithLifecycle()
        val title by createDocumentViewModel.title.collectAsStateWithLifecycle()
        val description by createDocumentViewModel.description.collectAsStateWithLifecycle()
        val date by createDocumentViewModel.date.collectAsStateWithLifecycle()
        val newField by createDocumentViewModel.newField.collectAsStateWithLifecycle()
        val newFieldValue by createDocumentViewModel.newFieldValue.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            createDocumentViewModel.event.collect { event ->
                when (event) {
                    CreateDocumentEvent.Back -> onBackClick()
                }
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
            onTitleChange = createDocumentViewModel::changeTitle,
            onDescriptionChange = createDocumentViewModel::changeDescription,
            onDateChange = createDocumentViewModel::changeDate,
            onCategoryChange = createDocumentViewModel::changeCategory,
            onNewFieldChange = createDocumentViewModel::changeNewField,
            onNewFieldValueChange = createDocumentViewModel::changeNewFieldValue,
            onBackClick = onBackClick,
            onAddContentClick = createDocumentViewModel::addContent,
            onDeleteContentItemClick = createDocumentViewModel::deleteContentItem,
        )
    }
}

fun NavController.navigateToCreateDocument() {
    navigate(CreateDocument)
}
package mefetran.dgusev.meddocs.ui.screen.documentcreate

import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mefetran.dgusev.meddocs.app.DOCUMENT_DESCRIPTION_LENGTH
import mefetran.dgusev.meddocs.app.DOCUMENT_FIELD_LENGTH
import mefetran.dgusev.meddocs.app.DOCUMENT_TITLE_LENGTH
import mefetran.dgusev.meddocs.app.DOCUMENT_VALUE_LENGTH
import mefetran.dgusev.meddocs.data.api.request.document.CreateDocumentRequestBody
import mefetran.dgusev.meddocs.data.model.Category
import mefetran.dgusev.meddocs.data.repository.DocumentRepository
import mefetran.dgusev.meddocs.di.RealRepository
import mefetran.dgusev.meddocs.ui.components.formatDate
import mefetran.dgusev.meddocs.ui.screen.documentcreate.model.CreateDocumentEvent
import mefetran.dgusev.meddocs.ui.screen.documentcreate.model.CreateDocumentState
import javax.inject.Inject

@HiltViewModel
class CreateDocumentViewModel @Inject constructor(
    @RealRepository private val documentRepository: DocumentRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateDocumentState())
    val state = _state.asStateFlow()

    private val _title = MutableStateFlow(TextFieldValue(""))
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow(TextFieldValue(""))
    val description = _description.asStateFlow()

    private val _date = MutableStateFlow(TextFieldValue(""))
    val date = _date.asStateFlow()

    private val _newField = MutableStateFlow(TextFieldValue(""))
    val newField = _newField.asStateFlow()

    private val _newFieldValue = MutableStateFlow(TextFieldValue(""))
    val newFieldValue = _newFieldValue.asStateFlow()

    private val _event = MutableSharedFlow<CreateDocumentEvent>()
    val event = _event.asSharedFlow()

    fun createDocument() {
        viewModelScope.launch {
            val createDocumentRequestBody = CreateDocumentRequestBody(
                title = _title.value.text,
                description = _description.value.text.ifBlank { null },
                date = _date.value.text.ifBlank { null },
                category = state.value.category.name.ifBlank { null },
                content = state.value.contentMap.ifEmpty { null }
            )

            val createDocumentResult = async {
                documentRepository.createDocument(createDocumentRequestBody).flowOn(dispatcher)
                    .first()
            }
            val newDocument = createDocumentResult.await()

            newDocument
                .onSuccess { document ->
                    documentRepository.saveDocumentLocal(document)
                    _event.emit(CreateDocumentEvent.Back)
                }
                .onFailure { throwable ->
                    Log.e("DocumentRepository", throwable.stackTraceToString())
                    _event.emit(CreateDocumentEvent.Back)
                }
        }
    }

    fun changeTitle(newValue: TextFieldValue) {
        if (newValue.text.length < DOCUMENT_TITLE_LENGTH) {
            _title.update { newValue }
        } else {
            _title.update {
                newValue.copy(
                    text = newValue.text.substring(0, DOCUMENT_TITLE_LENGTH), selection = TextRange(
                        DOCUMENT_TITLE_LENGTH
                    )
                )
            }
        }
    }

    fun changeDescription(newValue: TextFieldValue) {
        if (newValue.text.length < DOCUMENT_DESCRIPTION_LENGTH) {
            _description.update { newValue }
        } else {
            _description.update {
                newValue.copy(
                    text = newValue.text.substring(0, DOCUMENT_DESCRIPTION_LENGTH),
                    selection = TextRange(
                        DOCUMENT_DESCRIPTION_LENGTH
                    )
                )
            }
        }
    }

    fun changeNewField(newValue: TextFieldValue) {
        if (newValue.text.length < DOCUMENT_FIELD_LENGTH) {
            _newField.update { newValue }
        } else {
            _newField.update {
                newValue.copy(
                    text = newValue.text.substring(0, DOCUMENT_FIELD_LENGTH),
                    selection = TextRange(
                        DOCUMENT_FIELD_LENGTH
                    )
                )
            }
        }
    }

    fun changeNewFieldValue(newFieldValue: TextFieldValue) {
        if (newFieldValue.text.length < DOCUMENT_VALUE_LENGTH) {
            _newFieldValue.update { newFieldValue }
        } else {
            _newFieldValue.update {
                newFieldValue.copy(
                    text = newFieldValue.text.substring(0, DOCUMENT_VALUE_LENGTH),
                    selection = TextRange(
                        DOCUMENT_VALUE_LENGTH
                    )
                )
            }
        }
    }

    fun changeDate(timeMillis: Long?) {
        timeMillis?.let {
            _date.update { TextFieldValue(text = formatDate(timeMillis)) }
        }
    }

    fun changeCategory(newCategory: Category) {
        _state.update { it.copy(category = newCategory) }
    }

    fun addContent() {
        if (!state.value.contentMap.containsKey(newField.value.text)) {
            state.value.contentMap[newField.value.text] = newFieldValue.value.text
        }
    }

    fun deleteContentItem(field: String) {
        _state.value.contentMap.remove(key = field)
    }
}
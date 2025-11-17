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
import mefetran.dgusev.meddocs.app.DOCUMENT_CONTENT_ITEM_TITLE_LENGTH
import mefetran.dgusev.meddocs.app.DOCUMENT_TITLE_LENGTH
import mefetran.dgusev.meddocs.app.DOCUMENT_CONTENT_ITEM_DESC_LENGTH
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
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

    private val _documentTitle = MutableStateFlow(TextFieldValue(""))
    val documentTitle = _documentTitle.asStateFlow()

    private val _documentDescription = MutableStateFlow(TextFieldValue(""))
    val documentDescription = _documentDescription.asStateFlow()

    private val _date = MutableStateFlow(TextFieldValue(""))
    val date = _date.asStateFlow()

    private val _contentItemTitle = MutableStateFlow(TextFieldValue(""))
    val contentItemTitle = _contentItemTitle.asStateFlow()

    private val _contentItemDescription = MutableStateFlow(TextFieldValue(""))
    val contentItemDescription = _contentItemDescription.asStateFlow()

    private val _uiEvents = MutableSharedFlow<CreateDocumentEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun createDocument() {
        viewModelScope.launch {
            val createDocumentResult = async {
                documentRepository.createDocument(
                    title = _documentTitle.value.text,
                    description = _documentDescription.value.text.ifBlank { null },
                    date = _date.value.text.ifBlank { null },
                    category = state.value.category,
                    content = state.value.contentMap.ifEmpty { null },
                    file = null,
                ).flowOn(dispatcher)
                    .first()
            }
            val newDocument = createDocumentResult.await()

            newDocument
                .onSuccess { document ->
                    documentRepository.saveDocumentLocal(document)
                    _uiEvents.emit(CreateDocumentEvent.Back)
                }
                .onFailure { throwable ->
                    Log.e("DocumentRepository", throwable.stackTraceToString())
                    _uiEvents.emit(CreateDocumentEvent.Back)
                }
        }
    }

    fun changeDocumentTitle(newValue: TextFieldValue) {
        if (newValue.text.length < DOCUMENT_TITLE_LENGTH) {
            _documentTitle.update { newValue }
        } else {
            _documentTitle.update {
                newValue.copy(
                    text = newValue.text.substring(0, DOCUMENT_TITLE_LENGTH), selection = TextRange(
                        DOCUMENT_TITLE_LENGTH
                    )
                )
            }
        }
    }

    fun changeDocumentDescription(newValue: TextFieldValue) {
        if (newValue.text.length < DOCUMENT_DESCRIPTION_LENGTH) {
            _documentDescription.update { newValue }
        } else {
            _documentDescription.update {
                newValue.copy(
                    text = newValue.text.substring(0, DOCUMENT_DESCRIPTION_LENGTH),
                    selection = TextRange(
                        DOCUMENT_DESCRIPTION_LENGTH
                    )
                )
            }
        }
    }

    fun changeContentItemTitle(newValue: TextFieldValue) {
        if (newValue.text.length < DOCUMENT_CONTENT_ITEM_TITLE_LENGTH) {
            _contentItemTitle.update { newValue }
        } else {
            _contentItemTitle.update {
                newValue.copy(
                    text = newValue.text.substring(0, DOCUMENT_CONTENT_ITEM_TITLE_LENGTH),
                    selection = TextRange(
                        DOCUMENT_CONTENT_ITEM_TITLE_LENGTH
                    )
                )
            }
        }
    }

    fun changeContentItemDescription(newValue: TextFieldValue) {
        if (newValue.text.length < DOCUMENT_CONTENT_ITEM_DESC_LENGTH) {
            _contentItemDescription.update { newValue }
        } else {
            _contentItemDescription.update {
                newValue.copy(
                    text = newValue.text.substring(0, DOCUMENT_CONTENT_ITEM_DESC_LENGTH),
                    selection = TextRange(
                        DOCUMENT_CONTENT_ITEM_DESC_LENGTH
                    )
                )
            }
        }
    }

    fun changeDate(timeMillis: Long?) {
        timeMillis?.let {
            val currentTimeMillis = System.currentTimeMillis()
            _date.update { TextFieldValue(text = formatDate(if (timeMillis > currentTimeMillis) currentTimeMillis else timeMillis)) }
        }
    }

    fun changeCategory(newCategory: Category) {
        _state.update { it.copy(category = newCategory) }
    }

    fun addContentItem() {
        state.value.contentMap.putIfAbsent(
            contentItemTitle.value.text,
            contentItemDescription.value.text
        ).let {
            if (it == null) {
                _contentItemTitle.value = TextFieldValue("")
                _contentItemDescription.value = TextFieldValue("")
            }
        }
    }

    fun deleteContentItem(itemTitle: String) {
        _state.value.contentMap.remove(key = itemTitle)
    }
}

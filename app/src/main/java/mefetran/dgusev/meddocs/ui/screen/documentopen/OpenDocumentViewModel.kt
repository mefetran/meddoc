package mefetran.dgusev.meddocs.ui.screen.documentopen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.HttpStatusCode
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
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteDocumentLocalUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteDocumentRemoteUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentLocalUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentRemoteUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.UpdateDocumentLocalUseCase
import mefetran.dgusev.meddocs.ui.screen.documentopen.model.OpenDocumentEvent
import mefetran.dgusev.meddocs.ui.screen.documentopen.model.OpenDocumentState
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OpenDocumentViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val getDocumentLocalUseCase: GetDocumentLocalUseCase,
    private val getDocumentRemoteUseCase: GetDocumentRemoteUseCase,
    private val deleteDocumentRemoteUseCase: DeleteDocumentRemoteUseCase,
    private val deleteDocumentLocalUseCase: DeleteDocumentLocalUseCase,
    private val updateDocumentLocalUseCase: UpdateDocumentLocalUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(OpenDocumentState())
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<OpenDocumentEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private fun startLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    private fun stopLoading() {
        _state.update { it.copy(isLoading = false) }
    }

    fun loadDocument(documentId: String) {
        if (documentId.isBlank()) {
            return
        }

        viewModelScope.launch {
            startLoading()
            val getDocumentLocalParams = GetDocumentLocalUseCase.Params(documentId)
            val document = getDocumentLocalUseCase.execute(getDocumentLocalParams)
            document?.let {
                _state.update { currentState -> currentState.copy(document = document) }
                stopLoading()
                return@launch
            }

            val getDocumentRemoteParams = GetDocumentRemoteUseCase.Params(documentId)
            val documentDeferredResult = async {
                getDocumentRemoteUseCase.execute(getDocumentRemoteParams).flowOn(dispatcher).first()
            }
            val documentResult = documentDeferredResult.await()

            documentResult
                .onSuccess {
                    _state.update { currentState -> currentState.copy(document = it) }
                    stopLoading()
                }
                .onFailure {
                    _state.update { OpenDocumentState(isError = true) }
                }
        }
    }

    fun deleteDocument(documentId: String) {
        if (documentId.isBlank()) {
            return
        }

        viewModelScope.launch {
            val deleteDocumentRemoteParams = DeleteDocumentRemoteUseCase.Params(documentId)
            val deleteDocumentDeferredResult = async {
                deleteDocumentRemoteUseCase.execute(deleteDocumentRemoteParams)
            }
            val deleteDocumentResult = deleteDocumentDeferredResult.await()
            deleteDocumentResult
                .onSuccess {
                    val httpStatusCode = HttpStatusCode.fromValue(it)
                    if (httpStatusCode == HttpStatusCode.NoContent) {
                        val deleteDocumentLocalParams = DeleteDocumentLocalUseCase.Params(documentId)
                        deleteDocumentLocalUseCase.execute(deleteDocumentLocalParams)
                        _uiEvent.emit(OpenDocumentEvent.CloseScreen)
                    }
                }
                .onFailure {
                    _state.update { OpenDocumentState(isError = true) }
                }
        }
    }

    fun saveLocalFilePath(file: File) {
        viewModelScope.launch {
            startLoading()

            val localFilePath = file.absolutePath

            val updateDocumentLocalParams = UpdateDocumentLocalUseCase.Params(
                id = _state.value.document.id,
                localFilePath = localFilePath,
            )
            updateDocumentLocalUseCase.execute(updateDocumentLocalParams)

            val getDocumentLocalParams = GetDocumentLocalUseCase.Params(_state.value.document.id)
            val document = getDocumentLocalUseCase.execute(getDocumentLocalParams)
            stopLoading()
            document?.let {
                _state.update { currentState -> currentState.copy(document = document) }
                Log.d("MEDDOC", "The local file path: ${document.localFilePath}")
                return@launch
            }
        }

    }
}

package mefetran.dgusev.meddocs.ui.screen.documentopen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mefetran.dgusev.meddocs.data.repository.DocumentRepository
import mefetran.dgusev.meddocs.di.RealRepository
import mefetran.dgusev.meddocs.ui.screen.documentopen.model.OpenDocumentState
import javax.inject.Inject

@HiltViewModel
class OpenDocumentViewModel @Inject constructor(
    @RealRepository private val documentRepository: DocumentRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow(OpenDocumentState())
    val state = _state.asStateFlow()

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
            val document = documentRepository.getDocumentOrNullLocal(documentId)
            document?.let {
                _state.update { currentState -> currentState.copy(document = document) }
                stopLoading()
                return@launch
            }

            val documentDeferredResult = async {
                documentRepository.getDocumentById(documentId).flowOn(dispatcher).first()
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
}
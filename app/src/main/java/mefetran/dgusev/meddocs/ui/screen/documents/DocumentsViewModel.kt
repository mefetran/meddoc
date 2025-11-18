package mefetran.dgusev.meddocs.ui.screen.documents

import androidx.compose.runtime.toMutableStateList
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
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentsUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.ObserveDocumentsUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.SaveDocumentsListLocalUseCase
import mefetran.dgusev.meddocs.ui.screen.documents.model.DocumentsState
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val saveDocumentsListLocalUseCase: SaveDocumentsListLocalUseCase,
    private val observeDocumentsUseCase: ObserveDocumentsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(DocumentsState())
    val state = _state.asStateFlow()

    init {
        observeDocuments()
        loadDocuments()
    }

    private fun loadDocuments() {
        viewModelScope.launch {
            startLoading()
            val resultDeferred =
                async { getDocumentsUseCase.execute(Unit).flowOn(dispatcher).first() }
            val documents = resultDeferred.await()

            // TODO implement syncing documents from db to backend
            documents
                .onSuccess { newList ->
                    val saveParams = SaveDocumentsListLocalUseCase.Params(newList)
                    saveDocumentsListLocalUseCase.execute(saveParams)
                    stopLoading()
                }
                .onFailure { exception: Throwable ->
                    exception.printStackTrace()
                    stopLoading()
                }
        }
    }

    private fun observeDocuments() {
        viewModelScope.launch {
            observeDocumentsUseCase.execute(Unit).collect { newDocuments ->
                startLoading()
                _state.update { it.copy(documents = newDocuments.toMutableStateList()) }
                stopLoading()
            }
        }
    }

    private fun startLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    private fun stopLoading() {
        _state.update { it.copy(isLoading = false) }
    }
}

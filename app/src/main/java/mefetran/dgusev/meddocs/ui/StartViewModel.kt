package mefetran.dgusev.meddocs.ui

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.model.User
import mefetran.dgusev.meddocs.data.repository.UserRepository
import mefetran.dgusev.meddocs.di.FakeRepository
import mefetran.dgusev.meddocs.proto.Settings
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val dispatcher: CoroutineDispatcher,
    @FakeRepository val userRepository: UserRepository,
) : ViewModel() {
    private val _isLoadingState = MutableStateFlow(true)
    val isLoadingState = _isLoadingState.asStateFlow()

    private val _state = MutableStateFlow(StartState())
    val state = _state.asStateFlow()

    init {
        loadInitSettings()
    }

    private fun loadInitSettings() {
        runBlocking {
            val settings = settingsDataStore.data.first()

            _state.update {
                StartState(
                    darkThemeSettings = settings.darkThemeSettings,
                    bearerTokens = settings.bearerTokens,
                )
            }

            _isLoadingState.update { false }

            viewModelScope.launch { collectSettings() }
        }
    }

    private suspend fun collectSettings() {
        settingsDataStore.data.flowOn(dispatcher).collectLatest { currentSettings ->
            _state.update {
                it.copy(
                    darkThemeSettings = currentSettings.darkThemeSettings,
                    bearerTokens = currentSettings.bearerTokens,
                )
            }
        }
    }

    fun getUser(): User? = runBlocking(dispatcher) { userRepository.getUser() }
}
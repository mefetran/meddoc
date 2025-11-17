package mefetran.dgusev.meddocs.ui

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.data.api.TokenManager
import mefetran.dgusev.meddocs.di.RealRepository
import mefetran.dgusev.meddocs.proto.Settings
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val dispatcher: CoroutineDispatcher,
    @RealRepository val userRepository: UserRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {
    private val _isLoadingState = MutableStateFlow(true)
    val isLoadingState = _isLoadingState.asStateFlow()

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    private val _uiEvents =
        MutableSharedFlow<AppEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        observeTokenInvalidation()
        loadInitSettings()
    }

    private fun loadInitSettings() {
        runBlocking {
            tokenManager.checkAndUpdateToken()
            val user = userRepository.getUserOrNull()
            val settings = settingsDataStore.data.first()

            _state.update {
                AppState(
                    darkThemeSettings = settings.darkThemeSettings,
                    bearerTokens = settings.bearerTokens,
                    user = user,
                )
            }

            _isLoadingState.update { false }
        }
        viewModelScope.launch { collectSettings() }
    }

    private fun observeTokenInvalidation() {
        viewModelScope.launch {
            tokenManager.getTokenInvalidationEmitterFlow().collect {
                _uiEvents.emit(AppEvent.SignIn)
            }
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
}

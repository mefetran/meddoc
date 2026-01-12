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
import mefetran.dgusev.meddocs.app.datastore.isBlank
import mefetran.dgusev.meddocs.domain.usecase.user.GetUserUseCase
import mefetran.dgusev.meddocs.token.TokenManager
import mefetran.dgusev.meddocs.proto.Settings
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val dispatcher: CoroutineDispatcher,
    private val tokenManager: TokenManager,
    private val getUserUseCase: GetUserUseCase,
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

    fun unlockApp() {
        _state.update { it.copy(isAppLocked = false) }
    }

    fun lockApp() {
        _state.update {
            if (it.isPinEnabled) it.copy(isAppLocked = true)
            else it
        }
    }

    private fun loadInitSettings() {
        runBlocking {
            tokenManager.checkAndUpdateToken()
            val user = getUserUseCase.execute(Unit)
            val settings = settingsDataStore.data.first()

            val isAuthorized = !settings.bearerTokens.isBlank()

            _state.update {
                AppState(
                    darkThemeSettings = settings.darkThemeSettings,
                    bearerTokens = settings.bearerTokens,
                    user = user,
                    isPinEnabled = settings.securitySettings.pinEnabled,
                    isBiometricEnabled = settings.securitySettings.biometricEnabled,
                    isAppLocked = isAuthorized && settings.securitySettings.pinEnabled,
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
                _state.update { it.copy(isAppLocked = false) }
            }
        }
    }

    private suspend fun collectSettings() {
        settingsDataStore.data.flowOn(dispatcher).collectLatest { currentSettings ->
            val isAuthorized = !currentSettings.bearerTokens.isBlank()
            _state.update {
                it.copy(
                    darkThemeSettings = currentSettings.darkThemeSettings,
                    bearerTokens = currentSettings.bearerTokens,
                    isPinEnabled = currentSettings.securitySettings.pinEnabled,
                    isBiometricEnabled = currentSettings.securitySettings.biometricEnabled,
                    isAppLocked = isAuthorized && currentSettings.securitySettings.pinEnabled,
                )
            }
        }
    }
}

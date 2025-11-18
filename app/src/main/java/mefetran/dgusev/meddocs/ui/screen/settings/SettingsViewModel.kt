package mefetran.dgusev.meddocs.ui.screen.settings

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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mefetran.dgusev.meddocs.app.datastore.defaultSettings
import mefetran.dgusev.meddocs.app.datastore.withLanguage
import mefetran.dgusev.meddocs.app.datastore.withTheme
import mefetran.dgusev.meddocs.domain.usecase.user.DeleteUserUseCase
import mefetran.dgusev.meddocs.proto.Settings
import mefetran.dgusev.meddocs.ui.screen.settings.model.SettingsEvent
import mefetran.dgusev.meddocs.ui.screen.settings.model.SettingsState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val dispatcher: CoroutineDispatcher,
    private val deleteUserUseCase: DeleteUserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<SettingsEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.data.flowOn(dispatcher).collectLatest { settings ->
                _state.update {
                    it.copy(
                        useSystemTheme = settings.darkThemeSettings.useSystemSettings,
                        useDarkTheme = settings.darkThemeSettings.useDarkTheme,
                        currentLanguageCode = settings.currentLanguageCode,
                    )
                }
            }
        }
    }

    fun selectTheme(
        useDarkTheme: Boolean,
        useSystemTheme: Boolean,
    ) {
        viewModelScope.launch {
            settingsDataStore.updateData {
                it.withTheme(
                    useDarkTheme = useDarkTheme,
                    useSystemTheme = useSystemTheme
                )
            }
        }
    }

    fun selectLanguage(
        languageCode: String,
    ) {
        viewModelScope.launch {
            settingsDataStore.updateData {
                it.withLanguage(languageCode)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            deleteUserUseCase.execute(Unit)
            settingsDataStore.updateData { defaultSettings() }
            _uiEvents.emit(SettingsEvent.SignIn)
        }
    }
}

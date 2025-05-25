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
import mefetran.dgusev.meddocs.data.repository.UserRepository
import mefetran.dgusev.meddocs.di.RealRepository
import mefetran.dgusev.meddocs.proto.Settings
import mefetran.dgusev.meddocs.ui.screen.settings.model.SettingsEvent
import mefetran.dgusev.meddocs.ui.screen.settings.model.SettingsState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    @RealRepository private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<SettingsEvent>()
    val event = _event.asSharedFlow()

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
            userRepository.deleteUser()
            settingsDataStore.updateData { defaultSettings() }
            _event.emit(SettingsEvent.SignIn)
        }
    }
}
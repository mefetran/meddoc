package mefetran.dgusev.meddocs.ui.screen.main

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mefetran.dgusev.meddocs.proto.DarkThemeSettings
import mefetran.dgusev.meddocs.proto.Settings
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _isLoadingState = MutableStateFlow(true)
    val isLoadingState = _isLoadingState.asStateFlow()

    private val _darkThemeState = MutableStateFlow(
        DarkThemeSettings.newBuilder().setUseDarkTheme(true).setUseSystemSettings(true).build()
    )
    val darkThemeState = _darkThemeState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.data.flowOn(dispatcher).collectLatest { settings ->
                _darkThemeState.update {
                    settings.darkThemeSettings
                }
            }
        }
        viewModelScope.launch {
            delay(500)
            _isLoadingState.update { false }
        }
    }
}
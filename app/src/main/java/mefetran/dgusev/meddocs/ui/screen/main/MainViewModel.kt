package mefetran.dgusev.meddocs.ui.screen.main

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mefetran.dgusev.meddocs.proto.Settings
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsDataStore: DataStore<Settings>,
    dispatcher: CoroutineDispatcher,
) : ViewModel() {
    val currentLanguageState = settingsDataStore.data.map { it.currentLanguageCode }.flowOn(dispatcher).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "en"
    )
}
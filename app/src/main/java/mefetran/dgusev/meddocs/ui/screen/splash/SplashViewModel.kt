package mefetran.dgusev.meddocs.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _isLoadingState = MutableStateFlow(true)
    val isLoadingState = _isLoadingState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(500)
            _isLoadingState.update { false }
        }
    }
}
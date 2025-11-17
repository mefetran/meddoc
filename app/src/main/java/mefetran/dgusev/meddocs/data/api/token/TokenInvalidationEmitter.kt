package mefetran.dgusev.meddocs.data.api.token

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class TokenInvalidationEmitter @Inject constructor() {
    private val _invalidationFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val invalidationFlow = _invalidationFlow.asSharedFlow()

    suspend fun emitInvalidation() {
        _invalidationFlow.emit(Unit)
    }
}

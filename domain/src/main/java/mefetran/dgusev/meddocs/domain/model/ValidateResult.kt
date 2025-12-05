package mefetran.dgusev.meddocs.domain.model

sealed class ValidateResult {
    data object Success : ValidateResult()
    data class Error(val message: String) : ValidateResult()
}

package mefetran.dgusev.meddocs.data.model

sealed interface Result<out T> {
    data object Loading : Result<Nothing>
    data class Error(val exception: Exception) : Result<Nothing>
    data class Data<T>(val body: T) : Result<T>
}
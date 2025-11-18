package mefetran.dgusev.meddocs.domain.usecase

interface UseCase<Input, Output> {
    suspend fun execute(input: Input): Output
}

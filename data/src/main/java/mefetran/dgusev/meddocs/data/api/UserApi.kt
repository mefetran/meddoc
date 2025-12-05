package mefetran.dgusev.meddocs.data.api

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.data.api.request.user.UserRegistrationRequestBody
import mefetran.dgusev.meddocs.data.api.request.user.UserSignInRequestBody
import mefetran.dgusev.meddocs.data.api.response.user.TokenPairResponse
import mefetran.dgusev.meddocs.data.api.response.user.UserResponse

interface UserApi {
    suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<UserResponse>>

    suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>>
}

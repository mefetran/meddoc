package mefetran.dgusev.meddocs.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    @Serializable
    data object SignIn : Routes

    @Serializable
    data object SignUp : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object Settings : Routes
}
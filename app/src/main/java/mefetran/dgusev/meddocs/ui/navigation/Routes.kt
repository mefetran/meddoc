package mefetran.dgusev.meddocs.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    @Serializable
    data object Login : Routes

    @Serializable
    data object Registration : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object Settings : Routes
}
package mefetran.dgusev.meddocs.ui.components.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

internal data class BottomNavigationDataItem<T : Any>(
    @param:StringRes val localizedName: Int,
    val route: T,
    val icon: ImageVector,
)

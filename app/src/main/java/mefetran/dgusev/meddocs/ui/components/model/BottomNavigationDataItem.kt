package mefetran.dgusev.meddocs.ui.components.model

import androidx.compose.ui.graphics.vector.ImageVector

internal data class BottomNavigationDataItem<T : Any>(
    val name: String,
    val route: T,
    val icon: ImageVector,
)
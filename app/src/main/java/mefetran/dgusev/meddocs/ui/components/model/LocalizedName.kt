package mefetran.dgusev.meddocs.ui.components.model

import androidx.annotation.StringRes

internal interface LocalizedName {
    @get:StringRes
    val localizedName: Int
}
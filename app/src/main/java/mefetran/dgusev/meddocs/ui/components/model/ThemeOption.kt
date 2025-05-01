package mefetran.dgusev.meddocs.ui.components.model

import mefetran.dgusev.meddocs.R

internal sealed interface ThemeOption : LocalizedName {
    data object System : ThemeOption {
        override val localizedName: Int = R.string.theme_system_label
    }

    data object Dark : ThemeOption {
        override val localizedName: Int = R.string.theme_dark_label
    }

    data object Light : ThemeOption {
        override val localizedName: Int = R.string.theme_light_label
    }
}
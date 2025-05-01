package mefetran.dgusev.meddocs.ui.components.model

import mefetran.dgusev.meddocs.R

internal sealed interface LanguageOption : LocalizedName {
    val languageCode: String

    data object English : LanguageOption {
        override val localizedName: Int = R.string.language_english
        override val languageCode: String = "en"
    }

    data object Russian : LanguageOption {
        override val localizedName: Int = R.string.language_russian
        override val languageCode: String = "ru"
    }
}
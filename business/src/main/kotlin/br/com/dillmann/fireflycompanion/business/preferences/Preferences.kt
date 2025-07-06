package br.com.dillmann.fireflycompanion.business.preferences

data class Preferences(
    val requireBiometricLogin: Boolean = false,
    val theme: Theme = Theme.AUTO,
    val language: Language = Language.AUTO,
) {
    enum class Theme {
        AUTO,
        LIGHT,
        DARK,
    }

    enum class Language {
        AUTO,
        ENGLISH,
        PORTUGUESE,
    }
}

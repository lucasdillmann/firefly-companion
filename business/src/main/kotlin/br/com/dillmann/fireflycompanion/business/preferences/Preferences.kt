package br.com.dillmann.fireflycompanion.business.preferences

import java.io.Serializable

data class Preferences(
    val requireBiometricLogin: Boolean = false,
    val theme: Theme = Theme.AUTO,
    val language: Language = Language.AUTO,
) : Serializable {
    enum class Theme : Serializable {
        AUTO,
        LIGHT,
        DARK,
    }

    enum class Language : Serializable {
        AUTO,
        ENGLISH,
        PORTUGUESE,
    }
}

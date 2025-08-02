package br.com.dillmann.fireflycompanion.business.preferences

import java.io.Serializable

data class Preferences(
    val requireBiometricLogin: Boolean = false,
    val theme: Theme = Theme.AUTO,
    val language: Language = Language.AUTO,
    val lockTimeout: LockTimeout = LockTimeout.IMMEDIATELY,
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

    enum class LockTimeout(val seconds: Long) : Serializable {
        IMMEDIATELY(0),
        FIFTEEN_SECONDS(15),
        THIRTY_SECONDS(30),
        ONE_MINUTE(60),
        TWO_MINUTES(120),
        FIVE_MINUTES(300),
        TEN_MINUTES(600),
    }
}

package br.com.dillmann.fireflycompanion.business.preferences

import java.io.Serializable

data class Preferences(
    val requireBiometricLogin: Boolean = false,
    val theme: Theme = Theme.AUTO,
    val language: Language = Language.AUTO,
    val lockTimeout: LockTimeout = LockTimeout.IMMEDIATELY,
    val homePeriod: HomePeriod = HomePeriod.LAST_MONTH,
    val assistant: Assistant = Assistant(),
) : Serializable {
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

    enum class HomePeriod {
        WEEK_SO_FAR,
        LAST_WEEK,
        MONTH_SO_FAR,
        LAST_MONTH,
        YEAR_SO_FAR,
        LAST_YEAR,
        ALL_TIME,
    }

    enum class LockTimeout(val seconds: Long) {
        IMMEDIATELY(0),
        FIFTEEN_SECONDS(15),
        THIRTY_SECONDS(30),
        ONE_MINUTE(60),
        TWO_MINUTES(120),
        FIVE_MINUTES(300),
        TEN_MINUTES(600),
    }

    data class Assistant(
        val provider: AssistantProvider = AssistantProvider.DISABLED,
        val model: String? = null,
        val accessToken: String? = null,
        val url: String? = null,
    )

    enum class AssistantProvider {
        DISABLED,
        OPEN_AI,
        OPEN_AI_COMPATIBLE,
    }
}

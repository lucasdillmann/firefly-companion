package br.com.dillmann.fireflycompanion.android.core.extensions

import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.business.preferences.Preferences

fun Preferences.Theme.description(): String =
    when(this) {
        Preferences.Theme.AUTO -> i18n(R.string.theme_auto)
        Preferences.Theme.DARK -> i18n(R.string.theme_dark)
        Preferences.Theme.LIGHT -> i18n(R.string.theme_light)
    }

fun Preferences.Language.description(): String =
    when(this) {
        Preferences.Language.AUTO -> i18n(R.string.language_auto)
        Preferences.Language.ENGLISH -> i18n(R.string.language_english)
        Preferences.Language.PORTUGUESE -> i18n(R.string.language_portuguese)
    }

fun Preferences.LockTimeout.description(): String =
    when(this) {
        Preferences.LockTimeout.IMMEDIATELY -> i18n(R.string.lock_timeout_immediately)
        Preferences.LockTimeout.FIFTEEN_SECONDS -> i18n(R.string.lock_timeout_fifteen_seconds)
        Preferences.LockTimeout.THIRTY_SECONDS -> i18n(R.string.lock_timeout_thirty_seconds)
        Preferences.LockTimeout.ONE_MINUTE -> i18n(R.string.lock_timeout_one_minute)
        Preferences.LockTimeout.TWO_MINUTES -> i18n(R.string.lock_timeout_two_minutes)
        Preferences.LockTimeout.FIVE_MINUTES -> i18n(R.string.lock_timeout_five_minutes)
        Preferences.LockTimeout.TEN_MINUTES -> i18n(R.string.lock_timeout_ten_minutes)
    }

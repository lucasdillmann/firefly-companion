package br.com.dillmann.fireflycompanion.android.core.extensions

import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.R
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

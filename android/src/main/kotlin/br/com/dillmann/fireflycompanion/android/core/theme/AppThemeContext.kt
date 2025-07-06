package br.com.dillmann.fireflycompanion.android.core.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.dillmann.fireflycompanion.business.preferences.Preferences

object AppThemeContext {
    private val state = mutableStateOf(Preferences.Theme.AUTO)

    fun current(): State<Preferences.Theme> =
        state

    fun reconfigure(preferences: Preferences) {
        state.value = preferences.theme
    }
}

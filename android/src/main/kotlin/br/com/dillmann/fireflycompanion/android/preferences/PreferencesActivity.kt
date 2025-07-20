package br.com.dillmann.fireflycompanion.android.preferences

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesForm

class PreferencesActivity : PreconfiguredActivity() {

    @Composable
    override fun Content(padding: PaddingValues) {
        PreferencesForm(padding)
    }
}

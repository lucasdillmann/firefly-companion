package br.com.dillmann.fireflycompanion.android.preferences.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.business.preferences.Preferences

@Composable
fun PreferencesFormFields(
    state: MutableState<Preferences>,
    onChange: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        PreferencesFormSubtitle(text = i18n(R.string.general), modifier = Modifier)
        PreferencesFormBiometricsField(state, onChange)
        PreferencesFormSpacer()
        PreferencesFormThemeField(state, onChange)
        PreferencesFormSpacer()
        PreferencesFormLanguageField(state, onChange)
        PreferencesFormSpacer()

        PreferencesFormSubtitle(text = i18n(R.string.assistant_title))
        PreferencesFormAssistantField(state, onChange)
    }
}



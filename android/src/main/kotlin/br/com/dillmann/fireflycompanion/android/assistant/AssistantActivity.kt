package br.com.dillmann.fireflycompanion.android.assistant

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.R

class AssistantActivity: PreconfiguredActivity() {
    @Composable
    override fun Content(padding: PaddingValues) {
        Section(
            title = i18n(R.string.assistant_title),
            modifier = Modifier.padding(padding),
        ) {
            Text(
                text = i18n(R.string.not_implemented),
            )
        }
    }
}

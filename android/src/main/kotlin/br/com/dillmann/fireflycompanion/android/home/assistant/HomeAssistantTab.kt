package br.com.dillmann.fireflycompanion.android.home.assistant

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeAssistantTab() {
    Section(
        title = i18n(R.string.assistant_title),
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = i18n(R.string.not_implemented),
        )
    }
}

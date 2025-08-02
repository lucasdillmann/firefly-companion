package br.com.dillmann.fireflycompanion.android.preferences.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun PreferencesHeader() {
    Text(
        text = i18n(R.string.preferences),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(top = 64.dp, bottom = 16.dp)
    )
}

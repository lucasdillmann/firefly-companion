package br.com.dillmann.fireflycompanion.android.preferences.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PreferencesFormSubtitle(
    text: String,
    modifier: Modifier = Modifier.padding(top = 24.dp),
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(bottom = 16.dp).fillMaxWidth(),
    )
}

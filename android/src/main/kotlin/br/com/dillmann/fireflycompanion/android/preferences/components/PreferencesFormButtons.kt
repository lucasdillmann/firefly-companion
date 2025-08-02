package br.com.dillmann.fireflycompanion.android.preferences.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun PreferencesFormButtons(
    saveText: String = i18n(R.string.save),
    onSave: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        Button(onClick = onSave) {
            Text(text = saveText)
        }
    }

}

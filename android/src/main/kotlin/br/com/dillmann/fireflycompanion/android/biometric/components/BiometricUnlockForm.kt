package br.com.dillmann.fireflycompanion.android.biometric.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun BiometricUnlockForm(
    padding: PaddingValues,
    errorDialogVisible: MutableState<Boolean>,
    unlock: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = i18n(R.string.app_locked),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(top = 64.dp, bottom = 16.dp)
        )

        Text(
            text = i18n(R.string.authentication_required),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 0.dp, bottom = 32.dp)
        )

        Button(onClick = unlock) {
            Text(text = i18n(R.string.unlock))
        }
    }

    if (errorDialogVisible.value) {
        AlertDialog(
            onDismissRequest = { errorDialogVisible.value = false },
            title = { Text(text = i18n(R.string.authentication_failed)) },
            text = {
                Column {
                    Text(
                        text = i18n(R.string.authentication_failed_details),
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { errorDialogVisible.value = false }
                ) {
                    Text(i18n(R.string.got_it))
                }
            }
        )
    }
}

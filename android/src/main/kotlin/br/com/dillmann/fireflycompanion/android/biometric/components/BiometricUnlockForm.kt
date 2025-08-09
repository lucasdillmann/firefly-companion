package br.com.dillmann.fireflycompanion.android.biometric.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun BiometricUnlockForm(
    errorDialogVisible: MutableState<Boolean>,
    unlock: () -> Unit,
) {
    val authFailed by errorDialogVisible

    val title =
        if (authFailed) R.string.app_locked
        else R.string.authentication_failed
    val subtitle =
        if (authFailed) R.string.authentication_required
        else R.string.authentication_failed_details
    val buttonText =
        if (authFailed) R.string.unlock
        else R.string.try_again

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = i18n(title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = i18n(subtitle),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 64.dp)
        )

        Button(onClick = unlock) {
            Text(text = i18n(buttonText))
        }
    }
}

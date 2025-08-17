package br.com.dillmann.fireflycompanion.android.preferences.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.biometric.Biometrics
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.extensions.description
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.business.preferences.Preferences

@Composable
fun PreferencesFormBiometricsField(
    state: MutableState<Preferences>,
    onChange: () -> Unit,
) {
    val context = LocalContext.current
    val preferences by state
    var expanded by volatile(false)
    var warningVisible by volatile(false)

    fun testBiometrics() {
        Biometrics.test(context) {
            if (it == Biometrics.Outcome.ERROR)
                warningVisible = true

            if (it == Biometrics.Outcome.SUCCESS)
                state.value = preferences.copy(requireBiometricLogin = true)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = i18n(R.string.require_biometric_login),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = preferences.requireBiometricLogin,
            onCheckedChange = {
                if (it) testBiometrics()
                else state.value = preferences.copy(requireBiometricLogin = false)
            },
        )
    }

    if (preferences.requireBiometricLogin) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = i18n(R.string.lock_timeout),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )

            Box {
                Button(
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    content = { Text(text = preferences.lockTimeout.description()) },
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    Preferences.LockTimeout.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option.description()) },
                            onClick = {
                                state.value = preferences.copy(lockTimeout = option)
                                onChange()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }

    if (warningVisible) {
        AlertDialog(
            onDismissRequest = { state.value = preferences.copy(requireBiometricLogin = false) },
            title = { Text(text = i18n(R.string.not_supported)) },
            text = { Text(text = i18n(R.string.biometrics_not_supported)) },
            confirmButton = {
                TextButton(onClick = { warningVisible = false }) {
                    Text(i18n(R.string.got_it))
                }
            }
        )
    }
}

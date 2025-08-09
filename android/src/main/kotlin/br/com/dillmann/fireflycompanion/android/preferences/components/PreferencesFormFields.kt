package br.com.dillmann.fireflycompanion.android.preferences.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
fun PreferencesFormFields(
    state: MutableState<Preferences>,
    onChange: () -> Unit = {},
) {
    val context = LocalContext.current
    val preferences = state.value
    var themeExpanded by volatile(false)
    var languageExpanded by volatile(false)
    var lockTimeoutExpanded by volatile(false)
    var biometricsNotSupportedWarningVisible by volatile(false)

    fun testBiometrics() {
        Biometrics.test(context) {
            if (it == Biometrics.Outcome.ERROR)
                biometricsNotSupportedWarningVisible = true

            if (it == Biometrics.Outcome.SUCCESS)
                state.value = preferences.copy(requireBiometricLogin = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
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
                        onClick = { lockTimeoutExpanded = !lockTimeoutExpanded },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        content = { Text(text = preferences.lockTimeout.description()) },
                    )

                    DropdownMenu(
                        expanded = lockTimeoutExpanded,
                        onDismissRequest = { lockTimeoutExpanded = false },
                    ) {
                        Preferences.LockTimeout.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option.description()) },
                                onClick = {
                                    state.value = preferences.copy(lockTimeout = option)
                                    onChange()
                                    lockTimeoutExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = i18n(R.string.theme),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )

            Box {
                Button(
                    onClick = { themeExpanded = !themeExpanded },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    content = { Text(text = preferences.theme.description()) },
                )

                DropdownMenu(
                    expanded = themeExpanded,
                    onDismissRequest = { themeExpanded = false },
                ) {
                    Preferences.Theme.entries.forEach { themeOption ->
                        DropdownMenuItem(
                            text = { Text(text = themeOption.description()) },
                            onClick = {
                                state.value = preferences.copy(theme = themeOption)
                                onChange()
                                themeExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = i18n(R.string.language),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )

            Box {
                Button(
                    onClick = { languageExpanded = !languageExpanded },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    content = { Text(text = preferences.language.description()) },
                )

                DropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false },
                ) {
                    Preferences.Language.entries.forEach { languageOption ->
                        DropdownMenuItem(
                            text = { Text(text = languageOption.description()) },
                            onClick = {
                                state.value = preferences.copy(language = languageOption)
                                onChange()
                                languageExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }

    if (biometricsNotSupportedWarningVisible) {
        AlertDialog(
            onDismissRequest = { state.value = preferences.copy(requireBiometricLogin = false) },
            title = { Text(text = i18n(R.string.not_supported)) },
            text = { Text(text = i18n(R.string.biometrics_not_supported)) },
            confirmButton = {
                TextButton(onClick = { biometricsNotSupportedWarningVisible = false }) {
                    Text(i18n(R.string.got_it))
                }
            }
        )
    }
}

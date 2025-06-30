package br.com.dillmann.fireflycompanion.android.onboarding.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.business.preferences.Preferences

@Composable
fun OnboardingPreferencesFormFields(
    requireBiometricLogin: MutableState<Boolean>,
    theme: MutableState<Preferences.Theme>,
    onThemeChanged: (Preferences.Theme) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

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
                text = "Require biometric login",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = requireBiometricLogin.value,
                onCheckedChange = { requireBiometricLogin.value = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Theme",
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
                    content = { Text(text = theme.value.description) },
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    Preferences.Theme.entries.forEach { themeOption ->
                        DropdownMenuItem(
                            text = { Text(text = themeOption.description) },
                            onClick = {
                                theme.value = themeOption
                                onThemeChanged(themeOption)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

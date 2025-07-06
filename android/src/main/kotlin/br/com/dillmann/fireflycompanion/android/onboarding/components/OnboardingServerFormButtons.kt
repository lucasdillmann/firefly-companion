package br.com.dillmann.fireflycompanion.android.onboarding.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.R

@Composable
fun OnboardingServerFormButtons(onContinue: () -> Unit) {
    val showDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        OutlinedButton(
            onClick = { showDialog.value = true },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.secondary,
            )
        ) {
            Text(text = i18n(R.string.i_dont_have_a_token))
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = onContinue) {
            Text(text = i18n(R.string.continue_))
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Getting a token") },
            text = {
                Column {
                    Text(
                        text = i18n(R.string.onboarding_token_instructions_1),
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

                    Text(text = i18n(R.string.onboarding_token_instructions_2))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text(i18n(R.string.got_it))
                }
            }
        )
    }
}

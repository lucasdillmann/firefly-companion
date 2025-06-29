package br.com.dillmann.fireflycompanion.android.onboarding.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.ui.extensions.noLineBreaks

@Composable
fun OnboardingFormButtons(onContinue: () -> Unit) {
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
            Text(text = "I don't have a token")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = onContinue) {
            Text(text = "Continue")
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Getting a token") },
            text = {
                Column {
                    Text(
                        text = """
                            If you don't have a token yet, you can grab one by logging-in to your server using the 
                            Firefly III web interface, then on the left menu click on Options > Profile. 
                        """.noLineBreaks(),
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

                    Text(
                        text = """
                            After that, change to the "OAuth" tab and generate a new personal access token using the 
                            button located at the last section of the page.
                        """.noLineBreaks(),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text("Got it")
                }
            }
        )
    }
}

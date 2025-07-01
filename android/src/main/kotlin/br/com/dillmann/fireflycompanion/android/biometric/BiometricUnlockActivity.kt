package br.com.dillmann.fireflycompanion.android.biometric

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.ui.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.ui.extensions.noLineBreaks

class BiometricUnlockActivity : PreconfiguredActivity(allowAnonymous = true) {
    private val errorDialogVisible = mutableStateOf(false)

    @Composable
    override fun Content(padding: PaddingValues) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "App locked",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(top = 64.dp, bottom = 16.dp)
            )

            Text(
                text = "You need to authenticate before continuing",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 0.dp, bottom = 32.dp)
            )

            Button(onClick = ::unlock) {
                Text(text = "Unlock")
            }
        }

        if (errorDialogVisible.value) {
            AlertDialog(
                onDismissRequest = { errorDialogVisible.value = false },
                title = { Text(text = "Authentication failed") },
                text = {
                    Column {
                        Text(
                            text = """
                                Sorry, but we're unable to unlock the app at this moment: An unexpected error was found
                                while checking for your biometrics. Please try again.
                            """.noLineBreaks(),
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { errorDialogVisible.value = false }
                    ) {
                        Text("Got it")
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        unlock()
    }

    private fun unlock() {
        Biometrics.unlock(this) {
            if (it == Biometrics.Outcome.SUCCESS)
                finish()

            if (it == Biometrics.Outcome.ERROR)
                errorDialogVisible.value = true
        }
    }
}

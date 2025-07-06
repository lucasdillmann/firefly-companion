package br.com.dillmann.fireflycompanion.android.onboarding.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.extensions.noLineBreaks

@Composable
fun OnboardingServerFormHeader() {
    Text(
        text = "Let's get connected",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(top = 64.dp, bottom = 16.dp)
    )

    Text(
        text = """
            Please input your Firefly III server URL and personal access token.
        """.noLineBreaks(),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 0.dp, bottom = 32.dp)
    )
}

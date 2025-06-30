package br.com.dillmann.fireflycompanion.android.onboarding.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.ui.extensions.noLineBreaks

@Composable
fun OnboardingPreferencesHeader() {
    Text(
        text = "What suits you best?",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(top = 64.dp, bottom = 16.dp)
    )

    Text(
        text = """
            To finish things up select what you prefer. You can always change these options later in the settings
            page of the app.
        """.noLineBreaks(),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 0.dp, bottom = 32.dp)
    )
}

package br.com.dillmann.fireflycompanion.android.onboarding.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun OnboardingServerFormHeader() {
    Text(
        text = i18n(R.string.onboarding_server_title),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(top = 96.dp, bottom = 16.dp)
    )

    Text(
        text = i18n(R.string.onboarding_server_message),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 0.dp, bottom = 32.dp)
    )
}

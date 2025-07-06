package br.com.dillmann.fireflycompanion.android.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.section.SectionCard
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun HomeGoals() {
    SectionCard(
        title = i18n(R.string.goals)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = i18n(R.string.not_implemented),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

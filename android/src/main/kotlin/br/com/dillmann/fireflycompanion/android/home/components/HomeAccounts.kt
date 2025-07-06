package br.com.dillmann.fireflycompanion.android.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.ui.components.SectionCard

@Composable
fun HomeAccounts() {
    SectionCard(
        title = "Accounts"
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Not yet implemented",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

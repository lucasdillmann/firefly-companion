package br.com.dillmann.fireflycompanion.android.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.ui.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.ui.extensions.noLineBreaks

class HomeActivity : PreconfiguredActivity() {
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
                text = "Home",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(top = 64.dp, bottom = 16.dp)
            )

            Text(
                text = """
                    There's nothing implemented here. Yet.
                """.noLineBreaks(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 0.dp, bottom = 32.dp)
            )
        }
    }
}

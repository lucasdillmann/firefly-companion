package br.com.dillmann.fireflycompanion.android.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.dillmann.fireflycompanion.android.ui.activity.PreconfiguredActivity

class HomeActivity : PreconfiguredActivity() {
    @Composable
    override fun Content(padding: PaddingValues) {
        Text(text = "Hello there")
    }
}

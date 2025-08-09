package br.com.dillmann.fireflycompanion.android

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity

class SplashActivity : PreconfiguredActivity() {
    @Composable
    override fun Content(padding: PaddingValues) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

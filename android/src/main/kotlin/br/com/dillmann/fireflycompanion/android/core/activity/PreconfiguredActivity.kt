package br.com.dillmann.fireflycompanion.android.core.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.core.view.WindowCompat
import br.com.dillmann.fireflycompanion.android.biometric.BiometricUnlockActivity
import br.com.dillmann.fireflycompanion.android.biometric.Biometrics
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.theme.AppTheme
import br.com.dillmann.fireflycompanion.android.core.theme.AppThemeContext
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase

abstract class PreconfiguredActivity(
    private val allowAnonymous: Boolean = false,
) : ComponentActivity() {
    override fun onResume() {
        super.onResume()

        val preferences = getPreferences()
        if (preferences.requireBiometricLogin && Biometrics.locked && !allowAnonymous) {
            val intent = Intent(this, BiometricUnlockActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getPreferences()
        AppContext.reconfigure(preferences)
        AppThemeContext.reconfigure(preferences)

        if (preferences.requireBiometricLogin) {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE,
            )
        }

        enableEdgeToEdge()
        setContent {
            AppTheme {
                val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f

                SideEffect {
                    window?.let { window ->
                        WindowCompat.getInsetsController(window, window.decorView).apply {
                            isAppearanceLightStatusBars = !isDarkTheme
                            isAppearanceLightNavigationBars = !isDarkTheme
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    )

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        contentWindowInsets = WindowInsets(0)
                    ) { padding ->
                        Content(padding)
                    }
                }
            }
        }

    }

    private fun getPreferences(): Preferences =
        async { koin().get<GetPreferencesUseCase>().getPreferences() }.get()

    @Composable
    abstract fun Content(padding: PaddingValues)
}

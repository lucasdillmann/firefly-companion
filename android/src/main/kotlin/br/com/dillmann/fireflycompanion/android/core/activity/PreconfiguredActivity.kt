package br.com.dillmann.fireflycompanion.android.core.activity

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.biometric.BiometricUnlockActivity
import br.com.dillmann.fireflycompanion.android.biometric.Biometrics
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.refresh.RefreshDispatcher
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
            start<BiometricUnlockActivity>()
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
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Content(it)
                }
            }
        }
    }

    private fun getPreferences(): Preferences =
        async { koin().get<GetPreferencesUseCase>().getPreferences() }.get()


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller,
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

        if (resultCode == RESULT_OK)
            async { RefreshDispatcher.notify() }
    }

    @Composable
    abstract fun Content(padding: PaddingValues)
}

package br.com.dillmann.fireflycompanion.android.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.biometric.BiometricUnlockActivity
import br.com.dillmann.fireflycompanion.android.biometric.Biometrics
import br.com.dillmann.fireflycompanion.android.ui.theme.AppTheme
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import org.koin.java.KoinJavaComponent.getKoin
import java.util.concurrent.CompletableFuture

abstract class PreconfiguredActivity(
    private val allowAnonymous: Boolean = false,
) : ComponentActivity() {
    internal lateinit var preferences: Preferences
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        restoreState().whenComplete { _, _ ->
            if (preferences.requireBiometricLogin && !allowAnonymous) {
                window?.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE,
                )
            }

            enableEdgeToEdge()
            setContent {
                AppTheme(preferences) {
                    Scaffold(modifier = Modifier.fillMaxSize()) {
                        Content(it)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        restoreState().join()

        if (preferences.requireBiometricLogin && Biometrics.locked && !allowAnonymous)
            start<BiometricUnlockActivity>()
    }

    private fun restoreState(): CompletableFuture<Unit> {
        return if (!::preferences.isInitialized)
            async { preferences = getKoin().get<GetPreferencesUseCase>().getPreferences() }
        else
            CompletableFuture.completedFuture(Unit)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Biometrics.lock()
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    abstract fun Content(padding: PaddingValues)
}

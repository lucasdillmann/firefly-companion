package br.com.dillmann.fireflycompanion.android.biometric

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import br.com.dillmann.fireflycompanion.android.biometric.components.BiometricUnlockForm
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity

class BiometricUnlockActivity : PreconfiguredActivity(allowAnonymous = true) {
    private val errorDialogVisible = mutableStateOf(false)

    @Composable
    override fun Content(padding: PaddingValues) {
        BiometricUnlockForm(padding, errorDialogVisible, ::unlock)
    }

    override fun onResume() {
        super.onResume()
        unlock()
    }

    private fun unlock() {
        Biometrics.unlock(this) {
            if (it == Biometrics.Outcome.SUCCESS)
                finish()

            if (it == Biometrics.Outcome.ERROR)
                errorDialogVisible.value = true
        }
    }
}

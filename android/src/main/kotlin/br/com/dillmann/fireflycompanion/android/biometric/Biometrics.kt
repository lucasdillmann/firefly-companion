package br.com.dillmann.fireflycompanion.android.biometric

import br.com.dillmann.fireflycompanion.android.R
import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import java.util.concurrent.Executors

object Biometrics {
    enum class Outcome {
        SUCCESS,
        ERROR,
        KEPT_LOCKED,
    }

    var locked = true
        private set

    fun lock() {
        locked = true
    }

    fun unlock(context: Context, callback: (Outcome) -> Unit) {
        if (!locked) {
            callback(Outcome.SUCCESS)
            return
        }

        prompt(context) {
            if (it == Outcome.SUCCESS)
                locked = false

            callback(it)
        }
     }

    fun test(context: Context, callback: (Outcome) -> Unit) {
        prompt(context, callback)
    }

    private fun prompt(context: Context, callback: (Outcome) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        val prompt = BiometricPrompt
            .Builder(context)
            .setTitle(i18n(R.string.app_locked))
            .setSubtitle(i18n(R.string.use_biometrics_to_unlock))
            .setNegativeButton(i18n(R.string.cancel), executor) { _, _ -> callback(Outcome.KEPT_LOCKED) }
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()

        val biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                callback(Outcome.SUCCESS)
            }

            override fun onAuthenticationFailed() {
                callback(Outcome.KEPT_LOCKED)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                callback(Outcome.ERROR)
            }
        }

        val cancellationSignal = CancellationSignal()
        prompt.authenticate(cancellationSignal, executor, biometricCallback)
    }
}

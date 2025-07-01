package br.com.dillmann.fireflycompanion.android.biometric

import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import java.util.concurrent.Executors

object Biometrics {
    enum class Outcome {
        SUCCESS,
        ERROR,
        STILL_LOCKED,
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

        val executor = Executors.newSingleThreadExecutor()
        val prompt = BiometricPrompt
            .Builder(context)
            .setTitle("App locked")
            .setSubtitle("Use your biometric credentials to unlock and continue")
            .setNegativeButton("Cancel", executor) { _, _ -> callback(Outcome.STILL_LOCKED) }
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        val biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                locked = false
                callback(Outcome.SUCCESS)
            }

            override fun onAuthenticationFailed() {
                callback(Outcome.STILL_LOCKED)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                callback(Outcome.ERROR)
            }
        }

        val cancellationSignal = CancellationSignal()
        prompt.authenticate(cancellationSignal, executor, biometricCallback)
    }
}

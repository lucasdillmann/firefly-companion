package br.com.dillmann.fireflycompanion.android.biometric

import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import java.time.LocalDateTime
import java.util.concurrent.Executors

object Biometrics {
    enum class Outcome {
        SUCCESS,
        ERROR,
        KEPT_LOCKED,
    }

    var locked = true
        private set

    private var lockedAt: LocalDateTime? = null

    fun lock() {
        if (locked) return
        locked = true
        lockedAt = LocalDateTime.now()
    }

    fun unlock(context: Context, callback: (Outcome) -> Unit) {
        if (!locked || isWithinLockTimeout()) {
            locked = false
            lockedAt = null
            callback(Outcome.SUCCESS)
            return
        }

        prompt(context) {
            if (it == Outcome.SUCCESS) {
                locked = false
                lockedAt = null
            }

            callback(it)
        }
    }

    private fun isWithinLockTimeout(): Boolean {
        if (lockedAt == null)
            return false

        val preferences = async { get<GetPreferencesUseCase>().getPreferences() }.get()
        val skewSeconds = preferences.lockTimeout.seconds

        return lockedAt!!.plusSeconds(skewSeconds) > LocalDateTime.now()
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

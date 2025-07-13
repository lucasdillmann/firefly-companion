package br.com.dillmann.fireflycompanion.android.biometric

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object BiometricsLifecycle : DefaultLifecycleObserver {
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Biometrics.lock()
    }
}

package br.com.dillmann.fireflycompanion.android

import android.app.Application as AndroidApplication
import androidx.lifecycle.ProcessLifecycleOwner
import br.com.dillmann.fireflycompanion.android.biometric.BiometricsLifecycle
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibility
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager

class Application : AndroidApplication() {
    override fun onCreate() {
        super.onCreate()

        AppContext.init(this)
        KoinManager.init()
        MoneyVisibility.init(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(BiometricsLifecycle)
    }
}

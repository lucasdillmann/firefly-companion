package br.com.dillmann.fireflycompanion.android

import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.start
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibility
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.home.HomeActivity
import br.com.dillmann.fireflycompanion.android.onboarding.OnboardingStartActivity
import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.GetConfigUseCase

class MainActivity : PreconfiguredActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppContext.init(this)
        KoinManager.init()
        MoneyVisibility.init(this)

        super.onCreate(savedInstanceState)

        async {
            val serverConfig = koin().get<GetConfigUseCase>().getConfig()
            if (serverConfig == null)
                start<OnboardingStartActivity>()
            else
                start<HomeActivity>()

            finish()
        }.join()
    }

    @Composable
    override fun Content(padding: PaddingValues) {
        // NO-OP
    }
}

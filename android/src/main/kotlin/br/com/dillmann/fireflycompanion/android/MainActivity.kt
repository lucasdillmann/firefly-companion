package br.com.dillmann.fireflycompanion.android

import android.app.Activity
import android.content.Context
import android.os.Bundle
import br.com.dillmann.fireflycompanion.android.home.HomeActivity
import br.com.dillmann.fireflycompanion.android.onboarding.OnboardingStartActivity
import br.com.dillmann.fireflycompanion.android.ui.activity.start
import br.com.dillmann.fireflycompanion.business.BusinessModule
import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.GetConfigUseCase
import br.com.dillmann.fireflycompanion.core.CoreModule
import br.com.dillmann.fireflycompanion.database.DatabaseModule
import br.com.dillmann.fireflycompanion.thirdparty.ThirdPartyModule
import org.koin.android.ext.android.getKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : Activity() {
    init {
        startKoin {
            loadKoinModules(module {
                single<Context> { this@MainActivity }
            })

            modules(CoreModule, BusinessModule, DatabaseModule, ThirdPartyModule)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serverConfig = getKoin().get<GetConfigUseCase>().getConfig()

        if (serverConfig == null)
            start<OnboardingStartActivity>()
        else
            start<HomeActivity>()

        finish()
    }
}

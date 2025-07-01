package br.com.dillmann.fireflycompanion.android

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import br.com.dillmann.fireflycompanion.android.home.HomeActivity
import br.com.dillmann.fireflycompanion.android.onboarding.OnboardingStartActivity
import br.com.dillmann.fireflycompanion.android.ui.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.ui.activity.async
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

class MainActivity : PreconfiguredActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        startKoinIfNeeded()
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        async {
            val serverConfig = getKoin().get<GetConfigUseCase>().getConfig()
            if (serverConfig == null) {
                start<OnboardingStartActivity>()
                return@async
            }

            start<HomeActivity>()
            finish()
        }
    }

    private fun startKoinIfNeeded() {
        try {
            getKoin()
        } catch (_: IllegalStateException) {
            startKoin {
                loadKoinModules(module {
                    single<Context> { this@MainActivity }
                })

                loadKoinModules(CoreModule)
                loadKoinModules(BusinessModule)
                loadKoinModules(DatabaseModule)
                loadKoinModules(ThirdPartyModule)
            }
        }
    }

    @Composable
    override fun Content(padding: PaddingValues) {
        // NO-OP
    }
}

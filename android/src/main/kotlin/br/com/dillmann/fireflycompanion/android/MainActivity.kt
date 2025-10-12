package br.com.dillmann.fireflycompanion.android

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.core.router.Router
import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.GetConfigUseCase

class MainActivity : PreconfiguredActivity() {
    @Composable
    override fun Content(padding: PaddingValues) {
        val initialRoute by persistent {
            val serverConfig = get<GetConfigUseCase>().getConfig()
            if (serverConfig == null)
                Route.ONBOARDING_START_SCREEN
            else
                Route.HOME_SCREEN
        }

        if (initialRoute != null) {
            Router(initialRoute!!)
        }
    }
}

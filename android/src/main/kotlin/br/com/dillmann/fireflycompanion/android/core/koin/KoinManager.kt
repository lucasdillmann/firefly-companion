package br.com.dillmann.fireflycompanion.android.core.koin

import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.business.BusinessModule
import br.com.dillmann.fireflycompanion.core.CoreModule
import br.com.dillmann.fireflycompanion.database.DatabaseModule
import br.com.dillmann.fireflycompanion.database.context.ContextProvider
import br.com.dillmann.fireflycompanion.thirdparty.ThirdPartyModule
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

object KoinManager {
    private lateinit var instance: KoinApplication

    fun init() {
        if (::instance.isInitialized) return
        initKoin()
    }

    fun koin(): Koin =
        instance.koin

    private fun initKoin() {
        instance = startKoin {
            loadKoinModules(module {
                single<ContextProvider> { AppContext }
            })

            loadKoinModules(CoreModule)
            loadKoinModules(BusinessModule)
            loadKoinModules(DatabaseModule)
            loadKoinModules(ThirdPartyModule)
        }
    }
}

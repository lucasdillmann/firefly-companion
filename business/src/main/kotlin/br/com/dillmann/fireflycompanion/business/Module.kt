package br.com.dillmann.fireflycompanion.business

import br.com.dillmann.fireflycompanion.business.account.AccountModule
import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfigModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val BusinessModule =
    module {
        loadKoinModules(AccountModule)
        loadKoinModules(ServerConfigModule)
    }

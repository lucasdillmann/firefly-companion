package br.com.dillmann.fireflymobile.business

import br.com.dillmann.fireflymobile.business.serverconfig.ServerConfigModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val BusinessModule =
    module {
        loadKoinModules(ServerConfigModule)
    }

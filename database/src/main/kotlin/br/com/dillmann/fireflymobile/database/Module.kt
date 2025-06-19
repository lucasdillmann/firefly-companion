package br.com.dillmann.fireflymobile.database

import br.com.dillmann.fireflymobile.database.serverconfig.ServerConfigModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val DatabaseModule =
    module {
        loadKoinModules(ServerConfigModule)
    }

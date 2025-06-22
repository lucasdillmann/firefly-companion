package br.com.dillmann.fireflycompanion.database

import br.com.dillmann.fireflycompanion.database.serverconfig.ServerConfigModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val DatabaseModule =
    module {
        loadKoinModules(ServerConfigModule)
    }

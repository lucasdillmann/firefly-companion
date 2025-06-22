package br.com.dillmann.fireflycompanion.database.serverconfig

import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfigRepository
import org.koin.dsl.module

internal val ServerConfigModule =
    module {
        single<ServerConfigRepository> { ServerConfigPreferencesRepository(get()) }
    }

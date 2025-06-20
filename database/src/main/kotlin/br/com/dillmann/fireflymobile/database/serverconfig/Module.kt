package br.com.dillmann.fireflymobile.database.serverconfig

import br.com.dillmann.fireflymobile.business.serverconfig.ServerConfigRepository
import org.koin.dsl.module

internal val ServerConfigModule =
    module {
        single<ServerConfigRepository> { ServerConfigPreferencesRepository(get()) }
    }

package br.com.dillmann.fireflymobile.business.serverconfig

import org.koin.dsl.module

internal val ServerConfigModule =
    module {
        single<ServerConfigCommands> { ServerConfigService(get(), get()) }
        single { ServerConfigValidator() }
    }

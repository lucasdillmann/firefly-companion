package br.com.dillmann.fireflymobile.business.serverconfig

import br.com.dillmann.fireflymobile.business.serverconfig.command.GetServerConfigCommand
import br.com.dillmann.fireflymobile.business.serverconfig.command.SetServerConfigCommand
import org.koin.dsl.module

internal val ServerConfigModule =
    module {
        factory { ServerConfigService(get(), get()) }
        factory { SetServerConfigCommand { get<ServerConfigService>().setConfig(it) } }
        factory { GetServerConfigCommand { get<ServerConfigService>().getConfig() } }
    }

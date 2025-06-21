package br.com.dillmann.fireflymobile.business.serverconfig

import br.com.dillmann.fireflymobile.business.serverconfig.usecase.GetConfigUseCase
import br.com.dillmann.fireflymobile.business.serverconfig.usecase.SaveConfigUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val ServerConfigModule =
    module {
        single { ServerConfigValidator() }
        single { ServerConfigService(get(), get()) } binds arrayOf(
            GetConfigUseCase::class,
            SaveConfigUseCase::class,
        )
    }

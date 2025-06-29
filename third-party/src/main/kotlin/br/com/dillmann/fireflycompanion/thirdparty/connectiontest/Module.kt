package br.com.dillmann.fireflycompanion.thirdparty.connectiontest

import br.com.dillmann.fireflycompanion.business.connectiontest.ConnectionTestRepository
import org.koin.dsl.module

internal val ConnectionTestModule =
    module {
        single<ConnectionTestRepository> { ConnectionTestHttpRepository() }
    }

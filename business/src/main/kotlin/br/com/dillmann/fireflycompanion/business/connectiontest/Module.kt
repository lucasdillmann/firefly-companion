package br.com.dillmann.fireflycompanion.business.connectiontest

import org.koin.dsl.module

internal val ConnectionTestModule =
    module {
        single { ConnectionTestService(get()) }
    }

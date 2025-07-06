package br.com.dillmann.fireflycompanion.business.currency

import org.koin.dsl.module

internal val CurrencyModule =
    module {
        single { CurrencyService(get()) }
    }

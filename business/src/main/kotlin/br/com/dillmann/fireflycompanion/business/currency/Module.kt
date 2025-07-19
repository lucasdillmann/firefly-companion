package br.com.dillmann.fireflycompanion.business.currency

import br.com.dillmann.fireflycompanion.business.currency.usecase.GetDefaultCurrencyUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val CurrencyModule =
    module {
        single { CurrencyService(get()) } binds arrayOf(
            GetDefaultCurrencyUseCase::class,
        )
    }

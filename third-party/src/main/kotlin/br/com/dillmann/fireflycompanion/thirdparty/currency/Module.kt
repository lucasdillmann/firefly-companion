package br.com.dillmann.fireflycompanion.thirdparty.currency

import br.com.dillmann.fireflycompanion.business.currency.CurrencyRepository
import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.core.converter.getConverter
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.CurrenciesApi
import org.koin.dsl.module

internal val CurrencyModule =
    module {
        single { CurrenciesApi(get(Qualifiers.API_BASE_URL), get()) }
        single<CurrencyConverter> { getConverter() }
        single<CurrencyRepository> { CurrencyHttpRepository(get(), get()) }
    }

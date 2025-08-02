package br.com.dillmann.fireflycompanion.thirdparty.currency

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.currency.CurrencyRepository
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.CurrenciesApi

internal class CurrencyHttpRepository(
    private val api: CurrenciesApi,
    private val converter: CurrencyConverter,
) : CurrencyRepository {
    override suspend fun getDefault(): Currency {
        val currency = api.getNativeCurrency()
        return converter.toDomain(currency)
    }
}

package br.com.dillmann.fireflycompanion.thirdparty.currency

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.currency.CurrencyRepository
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.CurrenciesApi

internal class CurrencyHttpRepository(private val api: CurrenciesApi): CurrencyRepository {
    override suspend fun getDefault(): Currency =
        api.getNativeCurrency().toCurrency()
}

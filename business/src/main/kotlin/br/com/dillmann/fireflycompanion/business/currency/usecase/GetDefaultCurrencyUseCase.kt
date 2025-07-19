package br.com.dillmann.fireflycompanion.business.currency.usecase

import br.com.dillmann.fireflycompanion.business.currency.Currency

interface GetDefaultCurrencyUseCase {
    suspend fun getDefault(): Currency
}

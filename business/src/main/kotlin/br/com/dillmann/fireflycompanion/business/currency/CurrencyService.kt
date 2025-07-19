package br.com.dillmann.fireflycompanion.business.currency

import br.com.dillmann.fireflycompanion.business.currency.usecase.GetDefaultCurrencyUseCase

internal class CurrencyService(private val repository: CurrencyRepository) : GetDefaultCurrencyUseCase {
    override suspend fun getDefault() =
        repository.getDefault()
}

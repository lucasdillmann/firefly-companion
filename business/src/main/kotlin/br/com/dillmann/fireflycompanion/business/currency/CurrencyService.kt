package br.com.dillmann.fireflycompanion.business.currency

internal class CurrencyService(private val repository: CurrencyRepository) {
    suspend fun getDefault() =
        repository.getDefault()
}

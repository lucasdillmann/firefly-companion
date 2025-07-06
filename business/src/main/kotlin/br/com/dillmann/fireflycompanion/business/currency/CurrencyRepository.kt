package br.com.dillmann.fireflycompanion.business.currency

interface CurrencyRepository {
    suspend fun getDefault(): Currency
}

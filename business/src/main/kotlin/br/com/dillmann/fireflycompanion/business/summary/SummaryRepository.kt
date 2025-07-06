package br.com.dillmann.fireflycompanion.business.summary

import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.time.LocalDate

interface SummaryRepository {
    suspend fun getSummaryByCurrency(
        startDate: LocalDate,
        endDate: LocalDate,
        currency: Currency,
    ): Summary?
}

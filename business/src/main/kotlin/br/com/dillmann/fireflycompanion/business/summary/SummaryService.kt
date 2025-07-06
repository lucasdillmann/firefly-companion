package br.com.dillmann.fireflycompanion.business.summary

import br.com.dillmann.fireflycompanion.business.currency.CurrencyService
import br.com.dillmann.fireflycompanion.business.summary.usecase.GetSummaryUseCase
import java.time.LocalDate

internal class SummaryService(
    private val repository: SummaryRepository,
    private val currencyService: CurrencyService,
) : GetSummaryUseCase {
    override suspend fun getSummary(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Summary {
        val defaultCurrency = currencyService.getDefault()
        return repository.getSummaryByCurrency(startDate, endDate, defaultCurrency)
            ?: error("Unable to retrieve the summary for the default currency ${defaultCurrency.code}")
    }
}

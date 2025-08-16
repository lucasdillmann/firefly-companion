package br.com.dillmann.fireflycompanion.business.overview

import br.com.dillmann.fireflycompanion.business.currency.CurrencyService
import br.com.dillmann.fireflycompanion.business.overview.model.SummaryOverview
import br.com.dillmann.fireflycompanion.business.overview.usecase.ExpensesByCategoryOverviewUseCase
import br.com.dillmann.fireflycompanion.business.overview.usecase.SummaryOverviewUseCase
import java.time.LocalDate

internal class OverviewService(
    private val repository: OverviewRepository,
    private val currencyService: CurrencyService,
) : SummaryOverviewUseCase, ExpensesByCategoryOverviewUseCase {
    override suspend fun getSummary(
        startDate: LocalDate,
        endDate: LocalDate,
    ): SummaryOverview {
        val defaultCurrency = currencyService.getDefault()
        return repository.getSummaryByCurrency(startDate, endDate, defaultCurrency)
            ?: error("Unable to retrieve the summary for the default currency ${defaultCurrency.code}")
    }

    override suspend fun getExpensesByCategory(startDate: LocalDate, endDate: LocalDate) =
        repository.getExpensesByCategory(startDate, endDate)
}

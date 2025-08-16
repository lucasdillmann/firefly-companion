package br.com.dillmann.fireflycompanion.business.overview

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.overview.model.ExpensesByCategoryOverview
import br.com.dillmann.fireflycompanion.business.overview.model.SummaryOverview
import java.time.LocalDate

interface OverviewRepository {
    suspend fun getSummaryByCurrency(
        startDate: LocalDate,
        endDate: LocalDate,
        currency: Currency,
    ): SummaryOverview?

    suspend fun getExpensesByCategory(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<ExpensesByCategoryOverview>
}

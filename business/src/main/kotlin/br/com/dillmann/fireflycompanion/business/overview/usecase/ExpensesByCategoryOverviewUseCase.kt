package br.com.dillmann.fireflycompanion.business.overview.usecase

import br.com.dillmann.fireflycompanion.business.core.datetime.atEndOfMonth
import br.com.dillmann.fireflycompanion.business.core.datetime.atStartOfMonth
import br.com.dillmann.fireflycompanion.business.overview.model.ExpensesByCategoryOverview
import java.time.LocalDate

interface ExpensesByCategoryOverviewUseCase {
    suspend fun getExpensesByCategory(
        startDate: LocalDate = LocalDate.now().atStartOfMonth(),
        endDate: LocalDate = LocalDate.now().atEndOfMonth(),
    ): List<ExpensesByCategoryOverview>
}

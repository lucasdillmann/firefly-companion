package br.com.dillmann.fireflycompanion.business.account.usecase

import br.com.dillmann.fireflycompanion.business.account.AccountOverview
import java.time.LocalDate

interface GetAccountOverviewUseCase {
    suspend fun getOverview(
        startDate: LocalDate = LocalDate.now().minusMonths(1),
        endDate: LocalDate = LocalDate.now(),
    ): List<AccountOverview>
}

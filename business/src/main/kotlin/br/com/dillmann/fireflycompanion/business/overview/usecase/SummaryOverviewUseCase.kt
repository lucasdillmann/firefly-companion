package br.com.dillmann.fireflycompanion.business.overview.usecase

import br.com.dillmann.fireflycompanion.business.core.datetime.atEndOfMonth
import br.com.dillmann.fireflycompanion.business.core.datetime.atStartOfMonth
import br.com.dillmann.fireflycompanion.business.overview.model.SummaryOverview
import java.time.LocalDate

interface SummaryOverviewUseCase {
    suspend fun getSummary(
        startDate: LocalDate = LocalDate.now().atStartOfMonth(),
        endDate: LocalDate = LocalDate.now().atEndOfMonth(),
    ): SummaryOverview
}

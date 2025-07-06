package br.com.dillmann.fireflycompanion.business.summary.usecase

import br.com.dillmann.fireflycompanion.business.core.datetime.atEndOfMonth
import br.com.dillmann.fireflycompanion.business.core.datetime.atStartOfMonth
import br.com.dillmann.fireflycompanion.business.summary.Summary
import java.time.LocalDate

interface GetSummaryUseCase {
    suspend fun getSummary(
        startDate: LocalDate = LocalDate.now().atStartOfMonth(),
        endDate: LocalDate = LocalDate.now().atEndOfMonth(),
    ): Summary
}

package br.com.dillmann.fireflycompanion.business.transaction.usecase

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

interface ListTransactionsUseCase {
    suspend fun list(
        page: PageRequest,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
    ): Page<Transaction>
}

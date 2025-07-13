package br.com.dillmann.fireflycompanion.business.transaction.usecase

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.core.pagination.Page
import java.time.LocalDate

interface ListTransactionsUseCase {
    suspend fun list(
        pageNumber: Int = 0,
        pageSize: Int = 50,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
    ): Page<Transaction>
}

package br.com.dillmann.fireflycompanion.business.transaction.usecase

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.core.pagination.Page
import java.time.LocalDate

interface GetTransactionsUseCase {
    suspend fun getTransactions(
        pageNumber: Int = 0,
        pageSize: Int = 10,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
    ): Page<Transaction>
}

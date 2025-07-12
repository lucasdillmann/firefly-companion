package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.core.pagination.Page
import java.time.LocalDate

interface TransactionRepository {
    suspend fun getTransactions(
        pageNumber: Int,
        pageSize: Int,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction>
}

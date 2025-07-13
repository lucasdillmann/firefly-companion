package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.core.pagination.Page
import java.time.LocalDate

interface TransactionRepository {
    suspend fun list(
        pageNumber: Int,
        pageSize: Int,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction>

    suspend fun search(
        pageNumber: Int,
        pageSize: Int,
        terms: String,
    ): Page<Transaction>
}

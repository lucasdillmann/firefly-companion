package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

interface TransactionRepository {
    suspend fun list(
        page: PageRequest,
        accountId: String?,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Page<Transaction>

    suspend fun search(page: PageRequest, terms: String): Page<Transaction>

    suspend fun save(transaction: Transaction): Transaction

    suspend fun deleteById(id: String)
}

package br.com.dillmann.fireflycompanion.business.transaction.usecase

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.core.pagination.Page

interface SearchTransactionsUseCase {
    suspend fun search(
        pageNumber: Int = 0,
        pageSize: Int = 50,
        terms: String,
    ): Page<Transaction>
}

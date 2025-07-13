package br.com.dillmann.fireflycompanion.business.transaction.usecase

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

interface SearchTransactionsUseCase {
    suspend fun search(page: PageRequest, terms: String): Page<Transaction>
}

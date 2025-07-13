package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

internal class TransactionService(
    private val repository: TransactionRepository,
) : ListTransactionsUseCase, SearchTransactionsUseCase {
    override suspend fun list(
        page: PageRequest,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction> {
        return repository.list(page, startDate, endDate)
    }

    override suspend fun search(
        page: PageRequest,
        terms: String,
    ): Page<Transaction> {
        return repository.search(page, terms)
    }
}

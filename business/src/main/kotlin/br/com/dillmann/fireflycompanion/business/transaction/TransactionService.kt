package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.Page
import java.time.LocalDate

internal class TransactionService(
    private val repository: TransactionRepository,
) : ListTransactionsUseCase, SearchTransactionsUseCase {
    override suspend fun list(
        pageNumber: Int,
        pageSize: Int,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction> {
        return repository.list(pageNumber, pageSize, startDate, endDate)
    }

    override suspend fun search(
        pageNumber: Int,
        pageSize: Int,
        terms: String,
    ): Page<Transaction> {
        return repository.search(pageNumber, pageSize, terms)
    }
}

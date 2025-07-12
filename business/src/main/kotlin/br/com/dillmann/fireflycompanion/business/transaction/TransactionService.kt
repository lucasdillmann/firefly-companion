package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.usecase.GetTransactionsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.Page
import java.time.LocalDate

internal class TransactionService(
    private val repository: TransactionRepository,
) : GetTransactionsUseCase {
    override suspend fun getTransactions(
        pageNumber: Int,
        pageSize: Int,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction> {
        return repository.getTransactions(pageNumber, pageSize, startDate, endDate)
    }
}

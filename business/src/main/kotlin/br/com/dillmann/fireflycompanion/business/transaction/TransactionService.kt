package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SaveTransactionUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

internal class TransactionService(
    private val repository: TransactionRepository,
    private val validator: TransactionValidator,
) : ListTransactionsUseCase, SearchTransactionsUseCase, SaveTransactionUseCase {

    override suspend fun list(page: PageRequest, startDate: LocalDate?, endDate: LocalDate?): Page<Transaction> =
        repository.list(page, startDate, endDate)

    override suspend fun search(page: PageRequest, terms: String): Page<Transaction> =
        repository.search(page, terms)

    override suspend fun save(transaction: Transaction): Transaction {
        validator.validate(transaction)
        return repository.save(transaction)
    }
}

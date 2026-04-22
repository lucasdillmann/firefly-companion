package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.usecase.DeleteTransactionUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SaveTransactionUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SuggestTransactionCateogoryUseCase
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

internal class TransactionService(
    private val repository: TransactionRepository,
    private val validator: TransactionValidator,
) : ListTransactionsUseCase,
    SearchTransactionsUseCase,
    SaveTransactionUseCase,
    DeleteTransactionUseCase,
    SuggestTransactionCateogoryUseCase {

    override suspend fun list(
        page: PageRequest,
        accountId: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction> =
        repository.list(page, accountId, startDate, endDate)

    override suspend fun search(page: PageRequest, terms: String): Page<Transaction> =
        repository.search(page, terms)

    override suspend fun save(transaction: Transaction): Transaction {
        validator.validate(transaction)
        return repository.save(transaction)
    }

    override suspend fun delete(id: String) {
        repository.deleteById(id)
    }

    override suspend fun suggest(description: String, excludeTransactionId: String?): String? {
        val trimmedDescription = description.trim()
        if (trimmedDescription.isEmpty()) return null

        val page = repository.search(PageRequest(number = 0, size = 100), trimmedDescription)

        return page
            .filter { it.description.trim() == trimmedDescription }
            .filter { it.id != excludeTransactionId }
            .filter { !it.category.isNullOrBlank() }
            .maxByOrNull { it.date }
            ?.category
    }
}

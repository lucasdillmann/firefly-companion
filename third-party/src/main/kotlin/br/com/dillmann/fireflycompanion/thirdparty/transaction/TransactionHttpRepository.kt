package br.com.dillmann.fireflycompanion.thirdparty.transaction

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.TransactionRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import br.com.dillmann.fireflycompanion.thirdparty.core.firefly.toPage
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.SearchApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.TransactionsApi
import java.time.LocalDate

internal class TransactionHttpRepository(
    private val transactionApi: TransactionsApi,
    private val searchApi: SearchApi,
    private val converter: TransactionConverter,
) : TransactionRepository {
    override suspend fun list(page: PageRequest, startDate: LocalDate?, endDate: LocalDate?): Page<Transaction> {
        val response = transactionApi.listTransaction(
            page = page.number + 1,
            limit = page.size,
            start = startDate,
            end = endDate,
        )

        return response.meta.toPage(
            items = response.data,
            converter = converter::toDomain,
        )
    }

    override suspend fun search(page: PageRequest, terms: String): Page<Transaction> {
        val response = searchApi.searchTransactions(
            page = page.number + 1,
            limit = page.size,
            query = terms,
        )

        return response.meta.toPage(
            items = response.data,
            converter = converter::toDomain,
        )
    }

    override suspend fun save(transaction: Transaction): Transaction {
        val outputPayload =
            if (transaction.id != null) {
                val inputPayload = converter.toApiUpdate(transaction)
                transactionApi.updateTransaction(transaction.id!!, inputPayload)
            } else {
                val inputPayload = converter.toApiStore(transaction)
                transactionApi.storeTransaction(inputPayload)
            }

        return converter.toDomain(outputPayload.data)
    }

    override suspend fun deleteById(id: String) {
        transactionApi.deleteTransaction(id)
    }
}

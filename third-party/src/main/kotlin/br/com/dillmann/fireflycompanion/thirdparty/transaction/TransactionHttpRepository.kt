package br.com.dillmann.fireflycompanion.thirdparty.transaction

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.TransactionRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.thirdparty.core.toPage
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.SearchApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.TransactionsApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.TransactionRead
import java.time.LocalDate

internal class TransactionHttpRepository(
    private val mainApi: TransactionsApi,
    private val searchApi: SearchApi,
) : TransactionRepository {
    override suspend fun list(
        pageNumber: Int,
        pageSize: Int,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction> {
        val response = mainApi.listTransaction(
            page = pageNumber + 1,
            limit = pageSize,
            start = startDate?.toString(),
            end = endDate?.toString(),
        )

        return response.meta.toPage(
            items = response.data,
            converter = TransactionRead::toTransaction,
        )
    }

    override suspend fun search(
        pageNumber: Int,
        pageSize: Int,
        terms: String
    ): Page<Transaction> {
        val response = searchApi.searchTransactions(
            page = pageNumber + 1,
            limit = pageSize,
            query = terms,
        )

        return response.meta.toPage(
            items = response.data,
            converter = TransactionRead::toTransaction,
        )
    }
}

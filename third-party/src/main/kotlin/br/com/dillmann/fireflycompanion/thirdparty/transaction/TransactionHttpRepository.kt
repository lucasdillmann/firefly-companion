package br.com.dillmann.fireflycompanion.thirdparty.transaction

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.TransactionRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
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
        page: PageRequest,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction> {
        val response = mainApi.listTransaction(
            page = page.number + 1,
            limit = page.size,
            start = startDate?.toString(),
            end = endDate?.toString(),
        )

        return response.meta.toPage(
            items = response.data,
            converter = TransactionRead::toTransaction,
        )
    }

    override suspend fun search(
        page: PageRequest,
        terms: String
    ): Page<Transaction> {
        val response = searchApi.searchTransactions(
            page = page.number + 1,
            limit = page.size,
            query = terms,
        )

        return response.meta.toPage(
            items = response.data,
            converter = TransactionRead::toTransaction,
        )
    }
}

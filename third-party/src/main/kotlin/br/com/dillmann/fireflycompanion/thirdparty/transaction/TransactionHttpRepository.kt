package br.com.dillmann.fireflycompanion.thirdparty.transaction

import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.TransactionRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.TransactionsApi
import java.time.LocalDate

internal class TransactionHttpRepository(
    private val api: TransactionsApi,
) : TransactionRepository {
    override suspend fun getTransactions(
        pageNumber: Int,
        pageSize: Int,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Page<Transaction> {
        val response = api.listTransaction(
            page = pageNumber + 1,
            limit = pageSize,
            start = startDate?.toString(),
            end = endDate?.toString(),
        )

        val pagination = response.meta.pagination
        return Page(
            currentPage = pagination?.currentPage?.minus(1) ?: 0,
            totalPages = pagination?.totalPages ?: 0,
            pageSize = pagination?.perPage ?: 0,
            content = response.data.mapNotNull { it.toTransaction() },
        )
    }
}

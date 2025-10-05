package br.com.dillmann.fireflycompanion.thirdparty.summary

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.overview.OverviewRepository
import br.com.dillmann.fireflycompanion.business.overview.model.SummaryOverview
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.InsightApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.SummaryApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.TransactionsApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.AccountTypeProperty
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.BasicSummaryEntry
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.TransactionTypeFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import java.time.LocalDate

internal class OverviewHttpRepository(
    private val summaryApi: SummaryApi,
    private val transactions: TransactionsApi,
    private val insightApi: InsightApi,
    private val converter: SummaryConverter,
) : OverviewRepository {
    override suspend fun getSummaryByCurrency(
        startDate: LocalDate,
        endDate: LocalDate,
        currency: Currency,
    ): SummaryOverview =
        coroutineScope {
            val basicSummary = getBasicSummary(startDate, endDate, currency)
            val reconciliationAmount = getReconciliationAmount(startDate, endDate, currency)

            converter.toDomain(
                currency = currency,
                summary = basicSummary.await(),
                reconciliationAmount = reconciliationAmount.await(),
            )
        }

    override suspend fun getExpensesByCategory(startDate: LocalDate, endDate: LocalDate) =
        insightApi.insightExpenseCategory(start = startDate, end = endDate).map(converter::toDomain)

    private fun CoroutineScope.getReconciliationAmount(
        startDate: LocalDate,
        endDate: LocalDate,
        currency: Currency,
    ): Deferred<BigDecimal> =
        async {
            var pageNumber = 1
            val data = mutableListOf<BigDecimal>()

            do {
                val page =
                    transactions.listTransaction(
                        start = startDate,
                        end = endDate,
                        type = TransactionTypeFilter.RECONCILIATION,
                        page = pageNumber++,
                    )

                data += page
                    .data
                    .flatMap { it.attributes.transactions }
                    .filter { it.currencyId == currency.id }
                    .map {
                        if (it.sourceType == AccountTypeProperty.RECONCILIATION_ACCOUNT)
                            it.amount.abs()
                        else
                            it.amount.abs().negate()
                    }

            } while (page.data.isNotEmpty())

            data.sumOf { it }
        }

    private fun CoroutineScope.getBasicSummary(
        startDate: LocalDate,
        endDate: LocalDate,
        currency: Currency,
    ): Deferred<Map<String, BasicSummaryEntry>> =
        async {
            summaryApi.getBasicSummary(
                start = startDate,
                end = endDate,
                currencyCode = currency.code,
            )
        }
}

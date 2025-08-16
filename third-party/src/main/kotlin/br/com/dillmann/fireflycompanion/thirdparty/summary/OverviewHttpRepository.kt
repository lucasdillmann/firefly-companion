package br.com.dillmann.fireflycompanion.thirdparty.summary

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.overview.OverviewRepository
import br.com.dillmann.fireflycompanion.business.overview.model.SummaryOverview
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.InsightApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.SummaryApi
import java.time.LocalDate

internal class OverviewHttpRepository(
    private val summaryApi: SummaryApi,
    private val insightApi: InsightApi,
    private val converter: SummaryConverter,
) : OverviewRepository {
    override suspend fun getSummaryByCurrency(
        startDate: LocalDate,
        endDate: LocalDate,
        currency: Currency,
    ): SummaryOverview? {
        val response =
            summaryApi.getBasicSummary(
                start = startDate,
                end = endDate,
                currencyCode = currency.code,
            )

        return response
            .takeIf { it.isNotEmpty() }
            ?.let { converter.toDomain(currency, it) }
    }

    override suspend fun getExpensesByCategory(startDate: LocalDate, endDate: LocalDate) =
        insightApi.insightExpenseCategory(start = startDate, end = endDate).map(converter::toDomain)
}

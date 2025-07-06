package br.com.dillmann.fireflycompanion.thirdparty.summary

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.summary.Summary
import br.com.dillmann.fireflycompanion.business.summary.SummaryRepository
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.SummaryApi
import java.time.LocalDate

internal class SummaryHttpRepository(private val api: SummaryApi): SummaryRepository {
    override suspend fun getSummaryByCurrency(
        startDate: LocalDate,
        endDate: LocalDate,
        currency: Currency,
    ): Summary? {
        val response =
            api.getBasicSummary(
                start = startDate.toString(),
                end = endDate.toString(),
                currencyCode = currency.code,
            )

        return response
            .takeIf { it.isNotEmpty() }
            ?.toSummary(currency)
    }
}

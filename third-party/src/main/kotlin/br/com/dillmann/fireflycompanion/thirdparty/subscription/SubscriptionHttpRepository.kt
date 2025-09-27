package br.com.dillmann.fireflycompanion.thirdparty.subscription

import br.com.dillmann.fireflycompanion.business.subscription.Subscription
import br.com.dillmann.fireflycompanion.business.subscription.SubscriptionRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import br.com.dillmann.fireflycompanion.thirdparty.core.firefly.toPage
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.BillsApi
import java.time.LocalDate

internal class SubscriptionHttpRepository(
    private val billsApi: BillsApi,
    private val converter: SubscriptionConverter,
) : SubscriptionRepository {
    override suspend fun findRecurrences(
        page: PageRequest,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Page<Subscription> {
        val response = billsApi.listBill(
            start = startDate,
            end = endDate,
            page = page.number + 1,
            limit = page.size,
        )

        return response.meta.toPage(
            items = response.data.sortedBy { it.attributes.name },
            converter = converter::toDomain,
        )
    }
}

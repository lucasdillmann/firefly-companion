package br.com.dillmann.fireflycompanion.business.subscription

import br.com.dillmann.fireflycompanion.business.subscription.usecase.SubscriptionOverviewUseCase
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

internal class SubscriptionService(
    private val repository: SubscriptionRepository,
) : SubscriptionOverviewUseCase {
    override suspend fun subscriptionsOverview(
        page: PageRequest,
        startDate: LocalDate,
        endDate: LocalDate,
    ) = repository.findRecurrences(page, startDate, endDate)
}

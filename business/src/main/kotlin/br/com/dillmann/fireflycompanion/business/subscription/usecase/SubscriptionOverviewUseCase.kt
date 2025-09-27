package br.com.dillmann.fireflycompanion.business.subscription.usecase

import br.com.dillmann.fireflycompanion.business.subscription.Subscription
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

interface SubscriptionOverviewUseCase {
    suspend fun subscriptionsOverview(
        page: PageRequest,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Page<Subscription>
}

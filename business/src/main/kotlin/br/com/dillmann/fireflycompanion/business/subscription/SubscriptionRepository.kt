package br.com.dillmann.fireflycompanion.business.subscription

import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

interface SubscriptionRepository {
    suspend fun findRecurrences(
        page: PageRequest,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Page<Subscription>
}

package br.com.dillmann.fireflycompanion.thirdparty.goal

import br.com.dillmann.fireflycompanion.business.goal.Goal
import br.com.dillmann.fireflycompanion.business.goal.GoalRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import br.com.dillmann.fireflycompanion.thirdparty.core.firefly.toPage
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.PiggyBanksApi

internal class GoalHttpRepository(
    private val api: PiggyBanksApi,
    private val converter: GoalConverter,
) : GoalRepository {
    override suspend fun findGoals(page: PageRequest): Page<Goal> {
        val response = api.listPiggyBank(
            page = page.number + 1,
            limit = page.size,
        )

        return response.meta.toPage(
            items = response.data.sortedBy { it.attributes.name },
            converter = converter::toDomain,
        )
    }
}

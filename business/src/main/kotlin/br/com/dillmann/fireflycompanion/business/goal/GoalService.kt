package br.com.dillmann.fireflycompanion.business.goal

import br.com.dillmann.fireflycompanion.business.goal.usecase.ListGoalsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

internal class GoalService(
    private val repository: GoalRepository,
) : ListGoalsUseCase {
    override suspend fun listGoals(page: PageRequest) =
        repository.findGoals(page)
}

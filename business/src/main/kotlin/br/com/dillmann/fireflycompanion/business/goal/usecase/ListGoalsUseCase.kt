package br.com.dillmann.fireflycompanion.business.goal.usecase

import br.com.dillmann.fireflycompanion.business.goal.Goal
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

interface ListGoalsUseCase {
    suspend fun listGoals(page: PageRequest): Page<Goal>
}

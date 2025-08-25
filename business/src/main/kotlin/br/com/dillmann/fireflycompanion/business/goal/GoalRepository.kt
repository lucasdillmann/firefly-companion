package br.com.dillmann.fireflycompanion.business.goal

import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

interface GoalRepository {
    suspend fun findGoals(page: PageRequest): Page<Goal>
}

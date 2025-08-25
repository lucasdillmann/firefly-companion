package br.com.dillmann.fireflycompanion.business.assistant.functions

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.Function
import br.com.dillmann.fireflycompanion.business.goal.usecase.ListGoalsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.fetchAllPages

internal class GoalsAssistantFunction(
    private val useCase: ListGoalsUseCase,
) : AssistantFunction {
    override fun metadata() =
        Function(
            name = "getGoals",
            description = "Get goals (name, start date, end date, target amount, current amount, etc)",
            arguments = emptyList(),
        )

    override suspend fun execute(arguments: Map<String, Any?>?) =
        fetchAllPages { useCase.listGoals(it) }
}

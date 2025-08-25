package br.com.dillmann.fireflycompanion.business.assistant.functions

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.Function
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.FunctionArgument
import br.com.dillmann.fireflycompanion.business.overview.usecase.ExpensesByCategoryOverviewUseCase
import java.time.LocalDate

internal class ExpensesByCategoryAssistantFunction(
    private val useCase: ExpensesByCategoryOverviewUseCase,
) : AssistantFunction {
    override fun metadata() =
        Function(
            name = "getExpensesByCategory",
            description = "Get the expenses (amount only) by category",
            arguments = listOf(
                FunctionArgument(
                    name = "startDate",
                    type = "string",
                    description = "Start date (date only in ISO-8601 format)",
                    required = true,
                ),
                FunctionArgument(
                    name = "endDate",
                    type = "string",
                    description = "End date (date only in ISO-8601 format)",
                    required = true,
                ),
            )
        )

    override suspend fun execute(arguments: Map<String, Any?>?): Any {
        val startDate = getDate(arguments, "startDate") ?: return mapOf("error" to "Invalid start date")
        val endDate = getDate(arguments, "endDate") ?: return mapOf("error" to "Invalid end date")

        return useCase.getExpensesByCategory(
            startDate = startDate,
            endDate = endDate
        )
    }

    private fun getDate(arguments: Map<String, Any?>?, name: String): LocalDate? {
        val value = arguments?.get(name) as? String ?: return null
        return runCatching { LocalDate.parse(value) }.getOrNull()
    }
}

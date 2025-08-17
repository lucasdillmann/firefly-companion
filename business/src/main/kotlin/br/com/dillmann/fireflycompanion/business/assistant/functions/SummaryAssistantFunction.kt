package br.com.dillmann.fireflycompanion.business.assistant.functions

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.Function
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.FunctionArgument
import br.com.dillmann.fireflycompanion.business.overview.usecase.SummaryOverviewUseCase
import java.time.LocalDate

internal class SummaryAssistantFunction(
    private val useCase: SummaryOverviewUseCase,
) : AssistantFunction {
    override fun metadata() =
        Function(
            name = "get_summary_overview",
            description = "Get summary overview with all account's totals (net worth, earned, spent, bills paid, " +
                "unpaid bills, left to spend and balance) for a given period of time",
            arguments = listOf(
                FunctionArgument(
                    name = "start_date",
                    type = "string",
                    description = "Start date (date only in ISO-8601 format)",
                    required = true,
                ),
                FunctionArgument(
                    name = "end_date",
                    type = "string",
                    description = "End date (date only in ISO-8601 format)",
                    required = true,
                ),
            ),
        )

    override suspend fun execute(arguments: Map<String, Any?>?): Any? {
        val startDate = getDate(arguments, "start_date") ?: return mapOf("error" to "Invalid start date")
        val endDate = getDate(arguments, "end_date") ?: return mapOf("error" to "Invalid end date")

        return useCase.getSummary(startDate, endDate)
    }

    private fun getDate(arguments: Map<String, Any?>?, name: String): LocalDate? {
        val value = arguments?.get(name) as? String ?: return null
        return runCatching { LocalDate.parse(value) }.getOrNull()
    }
}

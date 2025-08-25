package br.com.dillmann.fireflycompanion.business.assistant.functions

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.Function
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.FunctionArgument
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.fetchAllPages
import java.time.LocalDate

internal class ListTransactionsAssistantFunction(
    private val useCase: ListTransactionsUseCase,
): AssistantFunction {
    override fun metadata() =
        Function(
            name = "listTransactions",
            description = "Get transactions (deposits, withdraws and transfers) for a given period of time",
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
                FunctionArgument(
                    name = "accountId",
                    type = listOf("string", "null"),
                    description = "ID of the account",
                    required = false,
                ),
            )
        )

    override suspend fun execute(arguments: Map<String, Any?>?): Any {
        val startDate = getDate(arguments, "startDate") ?: return mapOf("error" to "Invalid start date")
        val endDate = getDate(arguments, "endDate") ?: return mapOf("error" to "Invalid end date")
        val accountId = arguments?.get("accountId") as? String?

        return fetchAllPages {
            useCase.list(it, accountId, startDate, endDate)
        }
    }

    private fun getDate(arguments: Map<String, Any?>?, name: String): LocalDate? {
        val value = arguments?.get(name) as? String ?: return null
        return runCatching { LocalDate.parse(value) }.getOrNull()
    }
}

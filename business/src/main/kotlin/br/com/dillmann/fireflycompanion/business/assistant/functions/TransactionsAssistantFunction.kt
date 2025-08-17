package br.com.dillmann.fireflycompanion.business.assistant.functions

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.Function
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.FunctionArgument
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

internal class TransactionsAssistantFunction(
    private val useCase: ListTransactionsUseCase,
): AssistantFunction {
    override fun metadata() =
        Function(
            name = "get_transactions",
            description = "Get transactions (deposits, withdraws and transfers) for a given period of time",
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
                FunctionArgument(
                    name = "account_id",
                    type = listOf("string", "null"),
                    description = "ID of the account",
                    required = false,
                ),
            )
        )

    override suspend fun execute(arguments: Map<String, Any?>?): Any? {
        val startDate = getDate(arguments, "start_date") ?: return mapOf("error" to "Invalid start date")
        val endDate = getDate(arguments, "end_date") ?: return mapOf("error" to "Invalid end date")
        val accountId = arguments?.get("account_id") as? String?

        val result = mutableListOf<Transaction>()
        var pageNumber = 0
        do {
            val page = useCase.list(
                page = PageRequest(pageNumber++, 100),
                accountId = accountId,
                startDate = startDate,
                endDate = endDate
            )

            result += page.content
        } while (page.hasMorePages)

        return result
    }

    private fun getDate(arguments: Map<String, Any?>?, name: String): LocalDate? {
        val value = arguments?.get(name) as? String ?: return null
        return runCatching { LocalDate.parse(value) }.getOrNull()
    }
}

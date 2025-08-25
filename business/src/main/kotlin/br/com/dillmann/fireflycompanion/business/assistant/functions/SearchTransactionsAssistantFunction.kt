package br.com.dillmann.fireflycompanion.business.assistant.functions

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.Function
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.FunctionArgument
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

internal class SearchTransactionsAssistantFunction(
    private val useCase: SearchTransactionsUseCase,
): AssistantFunction {
    override fun metadata() =
        Function(
            name = "searchTransactions",
            description = "Search for transactions (deposits, withdraws and transfers)",
            arguments = listOf(
                FunctionArgument(
                    name = "terms",
                    type = "string",
                    description = "Search terms",
                    required = true,
                ),
            )
        )

    override suspend fun execute(arguments: Map<String, Any?>?): Any {
        val terms = arguments?.get("terms") as? String? ?: return mapOf("error" to "Invalid terms")

        val result = mutableListOf<Transaction>()
        var pageNumber = 0
        do {
            val page = useCase.search(
                page = PageRequest(pageNumber++, 100),
                terms = terms,
            )

            result += page.content
        } while (page.hasMorePages)

        return result
    }
}

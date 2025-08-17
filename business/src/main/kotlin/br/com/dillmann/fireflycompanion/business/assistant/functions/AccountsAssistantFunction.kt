package br.com.dillmann.fireflycompanion.business.assistant.functions

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.usecase.ListAccountsUseCase
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest.Function
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

internal class AccountsAssistantFunction(
    private val useCase: ListAccountsUseCase,
) : AssistantFunction {
    override fun metadata() =
        Function(
            name = "get_accounts",
            description = "Get accounts (assets, credit cards, etc) including its balance",
            arguments = emptyList(),
        )

    override suspend fun execute(arguments: Map<String, Any?>?): Any? {
        val result = mutableListOf<Account>()
        var pageNumber = 0
        do {
            val page = useCase.listAccounts(
                page = PageRequest(pageNumber++, 100),
            )

            result += page.content
        } while (page.hasMorePages)

        return result
    }
}

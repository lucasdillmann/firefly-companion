package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.business.account.usecase.ListAccountsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

internal class AccountService(private val repository: AccountRepository) : ListAccountsUseCase {
    override suspend fun listAccounts(page: PageRequest): Page<Account> =
        repository.findAccounts(page, "asset").filter { it.active }
}

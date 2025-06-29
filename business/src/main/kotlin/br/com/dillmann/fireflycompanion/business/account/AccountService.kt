package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

internal class AccountService(private val repository: AccountRepository) : GetAccountsUseCase {
    override suspend fun getAccounts(page: PageRequest): Page<Account> =
        repository.findAccounts(page)
}

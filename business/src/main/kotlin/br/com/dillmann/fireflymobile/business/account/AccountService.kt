package br.com.dillmann.fireflymobile.business.account

import br.com.dillmann.fireflymobile.business.account.usecase.GetAccountsUseCase
import br.com.dillmann.fireflymobile.core.pagination.Page
import br.com.dillmann.fireflymobile.core.pagination.PageRequest

internal class AccountService(private val repository: AccountRepository) : GetAccountsUseCase {
    override fun getAccounts(page: PageRequest): Page<Account> =
        repository.findAccounts(page)
}

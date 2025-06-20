package br.com.dillmann.fireflymobile.business.account

import br.com.dillmann.fireflymobile.core.pagination.Page
import br.com.dillmann.fireflymobile.core.pagination.PageRequest

internal class AccountService(private val repository: AccountRepository) : AccountCommands {
    override fun getAccounts(page: PageRequest): Page<Account> =
        repository.findAccounts(page)
}

package br.com.dillmann.fireflymobile.business.account.usecase

import br.com.dillmann.fireflymobile.business.account.Account
import br.com.dillmann.fireflymobile.core.pagination.Page
import br.com.dillmann.fireflymobile.core.pagination.PageRequest

fun interface GetAccountsUseCase {
    fun getAccounts(page: PageRequest): Page<Account>
}

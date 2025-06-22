package br.com.dillmann.fireflycompanion.business.account.usecase

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

fun interface GetAccountsUseCase {
    fun getAccounts(page: PageRequest): Page<Account>
}

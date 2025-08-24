package br.com.dillmann.fireflycompanion.business.account.usecase

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

interface ListAccountsUseCase {
    suspend fun listAccounts(
        page: PageRequest,
        type: Account.Type? = Account.Type.ASSET,
    ): Page<Account>
}

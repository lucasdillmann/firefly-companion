package br.com.dillmann.fireflycompanion.thirdparty.account

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.AccountRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AccountsApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.AccountRead
import br.com.dillmann.fireflycompanion.thirdparty.core.toPage

internal class AccountHttpRepository(private val api: AccountsApi) : AccountRepository {
    override suspend fun findAccounts(page: PageRequest): Page<Account> {
        val response = api.listAccount(
            page = page.number,
            limit = page.size,
        )

        return response.meta.toPage(response.data, AccountRead::toAccount)
    }
}

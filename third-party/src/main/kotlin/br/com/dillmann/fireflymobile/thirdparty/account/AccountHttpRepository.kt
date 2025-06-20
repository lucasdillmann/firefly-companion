package br.com.dillmann.fireflymobile.thirdparty.account

import br.com.dillmann.fireflymobile.business.account.Account
import br.com.dillmann.fireflymobile.business.account.AccountRepository
import br.com.dillmann.fireflymobile.core.pagination.Page
import br.com.dillmann.fireflymobile.core.pagination.PageRequest
import br.com.dillmann.fireflymobile.firefly.apis.AccountsApi
import br.com.dillmann.fireflymobile.firefly.models.AccountRead
import br.com.dillmann.fireflymobile.thirdparty.core.toPage

internal class AccountHttpRepository(private val api: AccountsApi) : AccountRepository {
    override fun findAccounts(page: PageRequest): Page<Account> {
        val response = api.listAccount(
            page = page.number,
            limit = page.size,
        )

        return response.meta.toPage(response.data, AccountRead::toAccount)
    }
}

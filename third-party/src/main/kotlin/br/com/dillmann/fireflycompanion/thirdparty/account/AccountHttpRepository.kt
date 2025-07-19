package br.com.dillmann.fireflycompanion.thirdparty.account

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.AccountRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AccountsApi
import br.com.dillmann.fireflycompanion.thirdparty.core.firefly.toPage
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.AccountTypeFilter

internal class AccountHttpRepository(
    private val api: AccountsApi,
    private val converter: AccountConverter
) : AccountRepository {
    override suspend fun findAccounts(page: PageRequest, type: String?): Page<Account> {
        val response = api.listAccount(
            page = page.number + 1,
            limit = page.size,
            type = AccountTypeFilter.decode(type),
        )

        return response.meta.toPage(response.data) { converter.toDomain(it) }
    }
}

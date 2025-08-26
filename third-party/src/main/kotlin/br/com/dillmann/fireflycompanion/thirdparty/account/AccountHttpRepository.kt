package br.com.dillmann.fireflycompanion.thirdparty.account

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.AccountOverview
import br.com.dillmann.fireflycompanion.business.account.AccountRepository
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import br.com.dillmann.fireflycompanion.thirdparty.core.firefly.toPage
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AccountsApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.ChartsApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.AccountTypeFilter
import java.time.LocalDate

internal class AccountHttpRepository(
    private val accountsApi: AccountsApi,
    private val chartsApi: ChartsApi,
    private val converter: AccountConverter,
) : AccountRepository {
    override suspend fun findAccounts(page: PageRequest, type: Account.Type?): Page<Account> {
        val response = accountsApi.listAccount(
            page = page.number + 1,
            limit = page.size,
            type = type?.name?.let(AccountTypeFilter::decode) ?: AccountTypeFilter.ALL,
            date = LocalDate.now().plusDays(1),
        )

        return response.meta.toPage(
            items = response.data.sortedBy { it.attributes.name },
            converter = converter::toDomain,
        )
    }

    override suspend fun findById(accountId: String): Account? =
        accountsApi.getAccount(accountId).data.let(converter::toDomain)

    override suspend fun findOverview(startDate: LocalDate, endDate: LocalDate): List<AccountOverview> =
        chartsApi
            .getChartAccountOverview(startDate, endDate)
            .map(converter::toDomain)
}

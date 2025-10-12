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
            date = date(),
        )

        return response.meta.toPage(
            items = response.data.sortedBy { it.attributes.name },
            converter = converter::toDomain,
        )
    }

    override suspend fun findActive(type: Account.Type?): List<Account> {
        val output = mutableListOf<Account>()
        var pageNumber = 0

        do {
            val page = findAccounts(PageRequest(pageNumber++, 100), type)
            output += page.content.filter { it.active }
        } while (page.hasMorePages)

        return output.sortedBy { it.name }
    }

    override suspend fun findById(accountId: String): Account =
        accountsApi.getAccount(id = accountId, date = date()).data.let(converter::toDomain)

    override suspend fun findOverview(startDate: LocalDate, endDate: LocalDate, type: Account.Type?): List<AccountOverview> =
        chartsApi
            .getChartAccountOverview(
                start = startDate,
                end = endDate,
                preselected = type?.toAccountOverviewType(),
                period = ChartsApi.PeriodGetChartAccountOverview._1_D,
            )
            .map(converter::toDomain)

    private fun Account.Type.toAccountOverviewType() =
        when (this) {
            Account.Type.ASSET -> ChartsApi.PreselectedGetChartAccountOverview.ASSETS
            Account.Type.LIABILITIES -> ChartsApi.PreselectedGetChartAccountOverview.LIABILITIES
            else -> error("Unsupported account type $this")
        }

    private fun date() =
        LocalDate.now().plusDays(1)
}

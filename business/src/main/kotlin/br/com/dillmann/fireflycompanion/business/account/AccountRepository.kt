package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate

interface AccountRepository {
    suspend fun findById(accountId: String): Account?
    suspend fun findAccounts(page: PageRequest, type: Account.Type? = null): Page<Account>
    suspend fun findOverview(startDate: LocalDate, endDate: LocalDate, type: Account.Type? = null): List<AccountOverview>
    suspend fun findActive(type: Account.Type? = null): List<Account>
}

package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

interface AccountRepository {
    suspend fun findAccounts(page: PageRequest, type: String? = null): Page<Account>
}

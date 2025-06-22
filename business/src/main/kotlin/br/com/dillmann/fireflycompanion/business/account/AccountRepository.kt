package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

interface AccountRepository {
    fun findAccounts(page: PageRequest): Page<Account>
}

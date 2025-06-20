package br.com.dillmann.fireflymobile.business.account

import br.com.dillmann.fireflymobile.core.pagination.Page
import br.com.dillmann.fireflymobile.core.pagination.PageRequest

interface AccountCommands {
    fun getAccounts(page: PageRequest): Page<Account>
}

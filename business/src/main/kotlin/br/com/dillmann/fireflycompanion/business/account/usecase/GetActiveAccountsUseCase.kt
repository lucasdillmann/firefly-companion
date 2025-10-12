package br.com.dillmann.fireflycompanion.business.account.usecase

import br.com.dillmann.fireflycompanion.business.account.Account

interface GetActiveAccountsUseCase {
    suspend fun getActiveAccounts(type: Account.Type? = null): List<Account>
}

package br.com.dillmann.fireflycompanion.business.account.usecase

import br.com.dillmann.fireflycompanion.business.account.Account

interface GetAccountUseCase {
    suspend fun getAccount(id: String): Account?
}

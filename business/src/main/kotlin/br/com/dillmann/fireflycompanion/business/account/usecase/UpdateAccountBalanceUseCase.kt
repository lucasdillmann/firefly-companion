package br.com.dillmann.fireflycompanion.business.account.usecase

import br.com.dillmann.fireflycompanion.business.account.Account
import java.math.BigDecimal

interface UpdateAccountBalanceUseCase {
    suspend fun updateBalance(accountId: String, newBalance: BigDecimal): Account
}

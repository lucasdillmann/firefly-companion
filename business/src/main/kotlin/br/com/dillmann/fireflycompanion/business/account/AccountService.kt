package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountOverviewUseCase
import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountUseCase
import br.com.dillmann.fireflycompanion.business.account.usecase.ListAccountsUseCase
import br.com.dillmann.fireflycompanion.business.account.usecase.UpdateAccountBalanceUseCase
import br.com.dillmann.fireflycompanion.business.currency.usecase.GetDefaultCurrencyUseCase
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SaveTransactionUseCase
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

internal class AccountService(
    private val repository: AccountRepository,
    private val defaultCurrency: GetDefaultCurrencyUseCase,
    private val saveTransaction: SaveTransactionUseCase,
) : ListAccountsUseCase, UpdateAccountBalanceUseCase, GetAccountUseCase, GetAccountOverviewUseCase {
    override suspend fun listAccounts(page: PageRequest, type: Account.Type?): Page<Account> =
        repository.findAccounts(page, type).filter { it.active }

    override suspend fun getAccount(id: String): Account? =
        repository.findById(id)

    override suspend fun updateBalance(accountId: String, newBalance: BigDecimal): Account {
        val account = repository.findById(accountId) ?: error("Account not found")
        val diff = newBalance - account.currentBalance
        if (diff.compareTo(BigDecimal.ZERO) == 0)
            return account

        val currency = defaultCurrency.getDefault()
        val transaction = Transaction(
            id = null,
            description = "Reconciliation",
            category = null,
            date = OffsetDateTime.now(),
            amount = diff.abs(),
            currency = currency,
            type = Transaction.Type.RECONCILIATION,
            sourceAccountName =
                if (diff < BigDecimal.ZERO) account.name
                else "${account.name} reconciliation (${currency.code})",
            destinationAccountName =
                if (diff > BigDecimal.ZERO) account.name
                else "${account.name} reconciliation (${currency.code})",
        )
        saveTransaction.save(transaction)

        return repository.findById(accountId)!!
    }

    override suspend fun getOverview(startDate: LocalDate, endDate: LocalDate): List<AccountOverview> =
        repository.findOverview(startDate, endDate)
}

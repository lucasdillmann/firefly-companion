package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.io.Serializable
import java.math.BigDecimal

data class Account(
    val id: String,
    val active: Boolean,
    val name: String,
    val currency: Currency,
    val openingBalance: BigDecimal,
    val currentBalance: BigDecimal,
    val type: Type,
    val includeInNetWorth: Boolean,
) : Serializable {
    enum class Type {
        ASSET,
        EXPENSE,
        IMPORT,
        REVENUE,
        CASH,
        LIABILITY,
        LIABILITIES,
        INITIAL_MINUS_BALANCE,
        RECONCILIATION,
    }
}

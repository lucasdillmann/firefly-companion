package br.com.dillmann.fireflycompanion.thirdparty.account

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.AccountRead
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.ShortAccountTypeProperty
import java.math.BigDecimal

internal fun AccountRead.toAccount(): Account =
    Account(
        id = id,
        type = attributes.type.toAccountType(),
        name = attributes.name,
        active = attributes.active ?: false,
        currentBalance = attributes.currentBalance?.toBigDecimal() ?: BigDecimal.ZERO,
        openingBalance = attributes.openingBalance?.toBigDecimal() ?: BigDecimal.ZERO,
        includeInNetWorth = attributes.includeNetWorth ?: false,
        currency = Currency(
            id = attributes.currencyId ?: "",
            code = attributes.currencyCode ?: "USD",
            symbol = attributes.currencySymbol ?: "$",
            decimalPlaces = attributes.currencyDecimalPlaces ?: 2,
        )
    )

private fun ShortAccountTypeProperty.toAccountType() =
    when (this) {
        ShortAccountTypeProperty.ASSET -> Account.Type.ASSET
        ShortAccountTypeProperty.CASH -> Account.Type.CASH
        ShortAccountTypeProperty.EXPENSE -> Account.Type.EXPENSE
        ShortAccountTypeProperty.IMPORT -> Account.Type.IMPORT
        ShortAccountTypeProperty.REVENUE -> Account.Type.REVENUE
        ShortAccountTypeProperty.LIABILITY -> Account.Type.LIABILITY
        ShortAccountTypeProperty.LIABILITIES -> Account.Type.LIABILITIES
        ShortAccountTypeProperty.INITIAL_MINUS_BALANCE -> Account.Type.INITIAL_MINUS_BALANCE
        ShortAccountTypeProperty.RECONCILIATION -> Account.Type.RECONCILIATION
    }

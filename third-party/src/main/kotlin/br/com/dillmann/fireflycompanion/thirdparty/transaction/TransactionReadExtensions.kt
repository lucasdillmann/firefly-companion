package br.com.dillmann.fireflycompanion.thirdparty.transaction

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.TransactionRead
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.TransactionTypeProperty
import java.math.BigDecimal
import java.time.OffsetDateTime

internal fun TransactionRead.toTransaction(): Transaction? {
    val split = attributes.transactions.firstOrNull() ?: return null

    return Transaction(
        id = id,
        description = split.description,
        date = OffsetDateTime.parse(split.date),
        amount = BigDecimal(split.amount),
        currency = Currency(
            id = split.currencyId ?: "",
            code = split.currencyCode ?: "USD",
            symbol = split.currencySymbol ?: "$",
            decimalPlaces = split.currencyDecimalPlaces ?: 2
        ),
        type = split.type.toTransactionType(),
        category = split.categoryName,
        sourceAccountName = split.sourceName,
        destinationAccountName = split.destinationName,
    )
}

private fun TransactionTypeProperty.toTransactionType(): Transaction.Type {
    return when (this) {
        TransactionTypeProperty.WITHDRAWAL -> Transaction.Type.WITHDRAWAL
        TransactionTypeProperty.DEPOSIT -> Transaction.Type.DEPOSIT
        TransactionTypeProperty.TRANSFER -> Transaction.Type.TRANSFER
        else -> Transaction.Type.UNKNOWN
    }
}

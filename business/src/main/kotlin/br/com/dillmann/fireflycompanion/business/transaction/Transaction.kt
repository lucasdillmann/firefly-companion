package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.io.Serializable
import java.math.BigDecimal
import java.time.OffsetDateTime

data class Transaction(
    val id: String,
    val description: String,
    val category: String?,
    val date: OffsetDateTime,
    val amount: BigDecimal,
    val currency: Currency,
    val type: Type,
    val sourceAccountName: String?,
    val destinationAccountName: String?,
) : Serializable {
    enum class Type : Serializable {
        WITHDRAWAL, DEPOSIT, TRANSFER, UNKNOWN
    }
}



package br.com.dillmann.fireflycompanion.business.subscription

import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class Subscription(
    val name: String,
    val currency: Currency,
    val active: Boolean,
    val expectedPayment: Payment?,
    val lastPayment: Payment?,
) : Serializable {
    data class Payment(
        val paidAt: LocalDate,
        val amount: BigDecimal,
    ) : Serializable
}

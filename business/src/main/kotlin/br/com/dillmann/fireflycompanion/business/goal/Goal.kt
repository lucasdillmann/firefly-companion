package br.com.dillmann.fireflycompanion.business.goal

import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.io.Serializable
import java.math.BigDecimal
import java.time.OffsetDateTime

data class Goal(
    val id: String,
    val name: String,
    val startDate: OffsetDateTime,
    val targetDate: OffsetDateTime,
    val currentAmount: BigDecimal,
    val targetAmount: BigDecimal,
    val currency: Currency,
    val active: Boolean,
) : Serializable

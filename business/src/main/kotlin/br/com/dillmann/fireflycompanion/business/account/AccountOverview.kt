package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.io.Serializable
import java.math.BigDecimal
import java.time.OffsetDateTime

data class AccountOverview(
    val name: String,
    val currency: Currency,
    val balanceHistory: Map<OffsetDateTime, BigDecimal>,
) : Serializable

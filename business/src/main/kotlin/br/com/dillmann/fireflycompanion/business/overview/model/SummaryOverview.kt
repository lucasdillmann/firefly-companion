package br.com.dillmann.fireflycompanion.business.overview.model

import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.io.Serializable
import java.math.BigDecimal

data class SummaryOverview(
    val currency: Currency,
    val netWorth: BigDecimal = BigDecimal.ZERO,
    val earned: BigDecimal? = null,
    val spent: BigDecimal? = null,
    val billsPaid: BigDecimal? = null,
    val unpaidBills: BigDecimal? = null,
    val leftToSpend: BigDecimal? = null,
    val balance: BigDecimal? = null,
) : Serializable

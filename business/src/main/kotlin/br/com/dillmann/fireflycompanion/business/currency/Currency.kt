package br.com.dillmann.fireflycompanion.business.currency

import java.io.Serializable
import java.math.BigDecimal

data class Currency(
    val id: String,
    val code: String,
    val symbol: String,
    val decimalPlaces: Int,
) : Serializable {
    fun format(value: BigDecimal): String =
        String.format("%s %,.${decimalPlaces}f", symbol, value)
}

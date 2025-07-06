package br.com.dillmann.fireflycompanion.thirdparty.summary

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.summary.Summary
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.BasicSummaryEntry
import java.math.BigDecimal

internal fun Map<String, BasicSummaryEntry>.toSummary(currency: Currency): Summary {
    val code = currency.code
    var output = Summary(currency)

    forEach { key, detail ->
        when(key) {
            "balance-in-$code" -> output = output.copy(balance = detail.monetaryValue)
            "spent-in-$code" -> output = output.copy(spent = detail.monetaryValue)
            "earned-in-$code" -> output = output.copy(earned = detail.monetaryValue)
            "bills-paid-in-$code" -> output = output.copy(billsPaid = detail.monetaryValue)
            "bills-unpaid-in-$code" -> output = output.copy(unpaidBills = detail.monetaryValue)
            "left-to-spend-in-$code" -> output = output.copy(leftToSpend = detail.monetaryValue)
            "net-worth-in-$code" -> output = output.copy(netWorth = detail.monetaryValue ?: BigDecimal.ZERO)
        }
    }

    return output
}

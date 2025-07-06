package br.com.dillmann.fireflycompanion.thirdparty.currency

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.CurrencySingle

internal fun CurrencySingle.toCurrency() =
    Currency(
        id = data.id,
        code = data.attributes.code,
        symbol = data.attributes.symbol,
        decimalPlaces = data.attributes.decimalPlaces ?: 2,
    )

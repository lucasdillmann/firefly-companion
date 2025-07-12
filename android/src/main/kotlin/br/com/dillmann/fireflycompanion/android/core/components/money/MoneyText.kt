package br.com.dillmann.fireflycompanion.android.core.components.money

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.math.BigDecimal

@Composable
fun MoneyText(
    value: BigDecimal?,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.primary,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    val visible by MoneyVisibility
    val text =
        if (visible.value) currency.format(value ?: BigDecimal.ZERO)
        else "${currency.symbol} •••"

    Row(
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = style,
            color = color,
        )
    }
}

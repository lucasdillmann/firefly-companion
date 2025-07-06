package br.com.dillmann.fireflycompanion.android.core.components.money

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
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    val visible by MoneyVisibility
    val text =
        if (visible.value) currency.format(value ?: BigDecimal.ZERO)
        else "${currency.symbol} •••"

    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier,
    )
}

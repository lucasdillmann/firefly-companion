package br.com.dillmann.fireflycompanion.android.core.components.money

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.math.BigDecimal

@Composable
fun MoneyText(
    value: BigDecimal?,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    dynamicColors: Boolean = false,
    baseColor: Color = MaterialTheme.colorScheme.primary,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    currency: Currency,
) {
    val visible by MoneyVisibility
    val text =
        if (visible.value) currency.format(value ?: BigDecimal.ZERO)
        else "${currency.symbol} •••"

    val tintColor = baseColor.copy(alpha = 0.5f)
    val color =
        when  {
            !dynamicColors || value == null || !visible.value -> baseColor
            value.compareTo(BigDecimal.ZERO) == 0 -> tintColor.compositeOver(Color.Gray)
            value < BigDecimal.ZERO -> tintColor.compositeOver(Color.Red)
            else -> tintColor.compositeOver(Color.Green)
        }

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

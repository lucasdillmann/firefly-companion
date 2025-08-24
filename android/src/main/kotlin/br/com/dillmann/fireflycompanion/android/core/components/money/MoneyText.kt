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
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    dynamicColors: Boolean = false,
    baseColor: Color = MoneyColor.base(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    currency: Currency,
) {
    val visible by MoneyVisibility
    val text = MoneyVisibility.format(value ?: BigDecimal.ZERO, currency)

    val color =
        when  {
            !dynamicColors || value == null || !visible.value -> baseColor
            else -> MoneyColor.of(value, baseColor)
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

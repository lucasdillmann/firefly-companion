package br.com.dillmann.fireflycompanion.android.core.components.money

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import br.com.dillmann.fireflycompanion.android.core.theme.AppColors
import java.math.BigDecimal

object MoneyColor {
    @Composable
    fun base() =
        MaterialTheme.colorScheme.primaryContainer

    @Composable
    fun positive(baseColor: Color = base()) =
        mix(baseColor, AppColors.Green)

    @Composable
    fun negative(baseColor: Color = base()) =
        mix(baseColor, AppColors.Red)

    @Composable
    fun internal(baseColor: Color = base()) =
        mix(baseColor, AppColors.Blue)

    @Composable
    fun neutral(baseColor: Color = base()) =
        mix(baseColor, AppColors.LightGray)

    @Composable
    fun of(value: BigDecimal, baseColor: Color = base()) =
        when {
            value.compareTo(BigDecimal.ZERO) == 0 -> neutral(baseColor)
            value < BigDecimal.ZERO -> negative(baseColor)
            else -> positive(baseColor)
        }

    private fun mix(base: Color, tint: Color) =
        tint.copy(alpha = 0.75f).compositeOver(base)
}

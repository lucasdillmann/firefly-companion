package br.com.dillmann.fireflycompanion.android.core.components.colorpool

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

object ColorPool {
    val Raw =
        setOf(
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Yellow,
            Color.Cyan,
            Color.Magenta,
            Color.LightGray,
        )

    fun indexed(index: Int): Color {
        val targetIndex = index % Raw.size
        val repetitions = index / Raw.size
        val alphaDecrease = (repetitions.toFloat() + 5) / 10
        return Raw
            .elementAt(targetIndex)
            .copy(alpha = 1f - alphaDecrease)
            .compositeOver(Color.Gray)
    }
}

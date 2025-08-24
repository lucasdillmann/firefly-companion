package br.com.dillmann.fireflycompanion.android.core.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

object AppTextFieldDefaults {
    val colors
        @Composable get() =
            TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
            )

    val textStyle
        @Composable get() =
            MaterialTheme.typography.bodyMedium

    @Composable
    fun Modifier.textFieldDefaults() =
        fillMaxHeight()
            .clip(RoundedCornerShape(25))
            .background(MaterialTheme.colorScheme.surfaceVariant)
}

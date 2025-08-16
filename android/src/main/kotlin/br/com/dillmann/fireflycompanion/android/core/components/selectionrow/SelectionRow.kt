package br.com.dillmann.fireflycompanion.android.core.components.selectionrow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.compose.volatile

@Composable
fun <T> SelectionRow(
    options: List<T>,
    modifier: Modifier = Modifier,
    pinSelectionToStart: Boolean = false,
    textRenderer: (T) -> String,
    onOptionSelected: (T) -> Unit,
    initialSelection: T? = options.first(),
    enabled: Boolean = true,
) {
    var selectedItem by volatile(initialSelection)
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (pinSelectionToStart && selectedItem != null) {
            ChipOption(
                text = textRenderer(selectedItem!!),
                selected = true,
                enabled = enabled,
                onClick = {
                    if (enabled) {
                        onOptionSelected(selectedItem!!)
                    }
                }
            )
        }

        Row(
            modifier = Modifier.horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options
                .filter { !pinSelectionToStart || it != selectedItem }
                .forEach { option ->
                    ChipOption(
                        text = textRenderer(option),
                        selected = selectedItem == option,
                        enabled = enabled,
                        onClick = {
                            if (enabled) {
                                selectedItem = option
                                onOptionSelected(option)
                            }
                        }
                    )
                }
        }
    }
}

@Composable
private fun ChipOption(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    var backgroundColor =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
    var textColor =
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant

    if (!enabled) {
        backgroundColor = backgroundColor.copy(alpha = 0.5f)
        textColor = textColor.copy(alpha = 0.5f)
    }

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

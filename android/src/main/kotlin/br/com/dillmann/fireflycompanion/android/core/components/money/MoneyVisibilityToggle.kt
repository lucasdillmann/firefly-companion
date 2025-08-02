package br.com.dillmann.fireflycompanion.android.core.components.money

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun MoneyVisibilityToggle(
    modifier: Modifier = Modifier,
) {
    val visible by MoneyVisibility
    val monetaryVisibilityIcon =
        if (visible.value) Icons.Filled.VisibilityOff
        else Icons.Filled.Visibility

    IconButton(
        onClick = MoneyVisibility::toggle,
        modifier = modifier.size(24.dp),
    ) {
        Icon(
            contentDescription = i18n(R.string.toggle_money_visibility),
            imageVector = monetaryVisibilityIcon,
        )
    }
}

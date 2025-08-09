package br.com.dillmann.fireflycompanion.android.home.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibilityToggle
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.core.router.navigate

@Composable
fun HomeTopActions() {
    val baseSize = MaterialTheme.typography.headlineSmall.lineHeight.value.dp

    MoneyVisibilityToggle(
        modifier = Modifier.size(baseSize),
    )

    IconButton(
        modifier = Modifier.size(baseSize),
        onClick = { navigate(Route.PREFERENCES_FORM) }
    ) {
        Icon(
            contentDescription = i18n(R.string.open_preferences),
            imageVector = Icons.Filled.Settings,
        )
    }
}

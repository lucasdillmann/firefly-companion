package br.com.dillmann.fireflycompanion.android.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.assistant.AssistantActivity
import br.com.dillmann.fireflycompanion.android.core.activity.start
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.transaction.TransactionFormActivity

@Composable
fun HomeQuickActions() {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SmallFloatingActionButton(
            onClick = { context.start<AssistantActivity>() },
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = i18n(R.string.ia_assistant),
                modifier = Modifier.size(16.dp),
            )
        }

        FloatingActionButton(
            onClick = { context.start<TransactionFormActivity>() },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = i18n(R.string.new_transaction),
            )
        }
    }
}

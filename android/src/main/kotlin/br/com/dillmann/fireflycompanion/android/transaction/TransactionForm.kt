package br.com.dillmann.fireflycompanion.android.transaction

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import br.com.dillmann.fireflycompanion.android.core.refresh.RefreshDispatcher
import br.com.dillmann.fireflycompanion.android.core.router.NavigationContext
import br.com.dillmann.fireflycompanion.android.transaction.components.TransactionDetails

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NavigationContext.TransactionForm() {
    TransactionDetails(
        transaction = getBagValue(),
        finish = {
            RefreshDispatcher.notify()
            finish()
        }
    )
}

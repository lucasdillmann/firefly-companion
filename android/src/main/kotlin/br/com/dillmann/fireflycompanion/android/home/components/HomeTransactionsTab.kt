package br.com.dillmann.fireflycompanion.android.home.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun HomeTransactionsTab() {
    Section(
        title = i18n(R.string.tab_transactions),
    ) {
        Text(i18n(R.string.not_implemented))
    }
}

package br.com.dillmann.fireflycompanion.android.home.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.dillmann.fireflycompanion.android.R

enum class HomeTabs(
    val title: Int,
    val icon: ImageVector,
) {
    MAIN(R.string.tab_main, Icons.Filled.Home),
    TRANSACTIONS(R.string.tab_transactions, Icons.Filled.SwapHoriz),
    ACCOUNTS(R.string.tab_accounts, Icons.Filled.AccountBalance),
    ASSISTANT(R.string.ia_assistant, Icons.Filled.AutoAwesome),
}

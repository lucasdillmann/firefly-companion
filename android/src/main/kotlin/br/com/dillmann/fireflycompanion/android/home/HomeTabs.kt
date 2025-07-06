package br.com.dillmann.fireflycompanion.android.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.dillmann.fireflycompanion.android.R

enum class HomeTabs(
    val title: Int,
    val icon: ImageVector,
) {
    MAIN(R.string.tab_main, Icons.Filled.Home),
    TRANSACTIONS(R.string.tab_transactions, Icons.Default.Wallet),
    MORE(R.string.tab_more, Icons.Filled.MoreHoriz)
}

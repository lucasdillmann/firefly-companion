package br.com.dillmann.fireflycompanion.android.home.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.components.pullrefresh.PullToRefreshWithScroll
import br.com.dillmann.fireflycompanion.android.core.refresh.RefreshDispatcher
import br.com.dillmann.fireflycompanion.android.home.HomeTabs

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeMainTab(
    modifier: Modifier = Modifier,
) {
    PullToRefreshWithScroll(
        onRefresh = { RefreshDispatcher.notify(HomeTabs.MAIN) },
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
    ) {
        item { HomeOverview() }
        item { HomeAccountsOverview() }
        item { HomeCreditCards() }
        item { HomeExpensesByCategory() }
        item { HomeBudgets() }
        item { HomeMonthlySavings() }
        item { HomeSpendFrequency() }
        item { HomeMonthlyBalance() }
        item { HomeGoals() }
    }
}

package br.com.dillmann.fireflycompanion.android.home.tabs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.persistent
import br.com.dillmann.fireflycompanion.android.core.components.pullrefresh.PullToRefreshWithScroll
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.home.components.*
import br.com.dillmann.fireflycompanion.business.summary.usecase.GetSummaryUseCase

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeMainTab(
    modifier: Modifier = Modifier,
) {
    val summaryUseCase = koin().get<GetSummaryUseCase>()
    var summary by persistent { summaryUseCase.getSummary() }

    fun reload() {
        summary = null

        async {
            summary = summaryUseCase.getSummary()
        }
    }

    OnRefreshEvent {
        reload()
    }

    PullToRefreshWithScroll(
        onRefresh = ::reload,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
    ) {
        item { HomeOverview(summary) }
        item { HomeCreditCards() }
        item { HomeExpensesByCategory() }
        item { HomeBudgets() }
        item { HomeMonthlySavings() }
        item { HomeSpendFrequency() }
        item { HomeMonthlyBalance() }
        item { HomeGoals() }
    }
}

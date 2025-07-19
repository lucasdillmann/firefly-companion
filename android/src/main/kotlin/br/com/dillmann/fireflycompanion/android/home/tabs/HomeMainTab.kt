package br.com.dillmann.fireflycompanion.android.home.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.state
import br.com.dillmann.fireflycompanion.android.core.components.pullrefresh.PullToRefreshWithScroll
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.home.components.HomeAccounts
import br.com.dillmann.fireflycompanion.android.home.components.HomeBudgets
import br.com.dillmann.fireflycompanion.android.home.components.HomeCreditCards
import br.com.dillmann.fireflycompanion.android.home.components.HomeExpensesByCategory
import br.com.dillmann.fireflycompanion.android.home.components.HomeGoals
import br.com.dillmann.fireflycompanion.android.home.components.HomeMonthlyBalance
import br.com.dillmann.fireflycompanion.android.home.components.HomeMonthlySavings
import br.com.dillmann.fireflycompanion.android.home.components.HomeOverview
import br.com.dillmann.fireflycompanion.android.home.components.HomeSpendFrequency
import br.com.dillmann.fireflycompanion.business.summary.usecase.GetSummaryUseCase

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeMainTab() {
    val summaryUseCase = koin().get<GetSummaryUseCase>()
    var summary by state { summaryUseCase.getSummary() }

    fun reload() {
        summary = null

        async {
            summary = summaryUseCase.getSummary()
        }
    }

    PullToRefreshWithScroll(
        onRefresh = ::reload,
        modifier = Modifier.fillMaxSize(),
    ) {
        HomeOverview(summary)
        HomeCreditCards()
        HomeAccounts()
        HomeExpensesByCategory()
        HomeBudgets()
        HomeMonthlySavings()
        HomeSpendFrequency()
        HomeMonthlyBalance()
        HomeGoals()
    }
}

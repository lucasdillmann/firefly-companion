package br.com.dillmann.fireflycompanion.android.home.tabs

import android.app.Activity.RESULT_OK
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.result.ResultNotifier
import br.com.dillmann.fireflycompanion.android.core.activity.state
import br.com.dillmann.fireflycompanion.android.core.components.pullrefresh.PullToRefreshWithScroll
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
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
fun HomeMainTab(
    resultNotifier: ResultNotifier,
    modifier: Modifier = Modifier,
) {
    val summaryUseCase = koin().get<GetSummaryUseCase>()
    var summary by state { summaryUseCase.getSummary() }

    fun reload() {
        summary = null

        async {
            summary = summaryUseCase.getSummary()
        }
    }

    fun handleResult(requestCode: Int, resultCode: Int) {
        if (requestCode == HomeTabs.MAIN.ordinal && resultCode == RESULT_OK)
            reload()
    }

    LaunchedEffect(Unit) {
        resultNotifier.subscribe(::handleResult)
    }

    DisposableEffect(Unit) {
        onDispose { resultNotifier.unsubscribe(::handleResult) }
    }

    PullToRefreshWithScroll(
        onRefresh = ::reload,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
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

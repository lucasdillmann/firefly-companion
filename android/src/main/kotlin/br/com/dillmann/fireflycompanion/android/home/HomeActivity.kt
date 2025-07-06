package br.com.dillmann.fireflycompanion.android.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.home.components.HomeBalance
import br.com.dillmann.fireflycompanion.android.ui.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.ui.activity.async
import br.com.dillmann.fireflycompanion.android.ui.activity.state
import br.com.dillmann.fireflycompanion.android.ui.components.PullToRefreshBox
import br.com.dillmann.fireflycompanion.business.summary.usecase.GetSummaryUseCase
import org.koin.android.ext.android.getKoin

class HomeActivity : PreconfiguredActivity() {
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content(padding: PaddingValues) {
        val summaryUseCase = getKoin().get<GetSummaryUseCase>()
        var summary by state { summaryUseCase.getSummary() }
        var reloading by state(false)

        fun reload() {
            reloading = true
            summary = null

            async {
                summary = summaryUseCase.getSummary()
                reloading = false
            }
        }

        PullToRefreshBox(
            isRefreshing = reloading,
            onRefresh = ::reload,
            modifier = Modifier.padding(padding).fillMaxSize(),
        ) {
            HomeBalance(summary)
        }
    }
}


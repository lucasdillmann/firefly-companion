package br.com.dillmann.fireflycompanion.android.core.components.pullrefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.persistent
import java.lang.Thread.sleep

@Composable
@ExperimentalMaterial3Api
fun PullToRefresh(
    onRefresh: suspend () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val pullState = rememberPullToRefreshState()
    var refreshing by persistent(false)

    fun handleRefresh() {
        refreshing = true

        async {
            onRefresh()
            sleep(100)
            refreshing = false
        }
    }

    Box(
        modifier
            .pullToRefresh(
                state = pullState,
                isRefreshing = refreshing,
                onRefresh = ::handleRefresh,
                enabled = enabled,
            )
            .fillMaxHeight(),
        contentAlignment = Alignment.TopStart,
    ) {
        content()

        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = refreshing,
            state = pullState,
        )
    }
}

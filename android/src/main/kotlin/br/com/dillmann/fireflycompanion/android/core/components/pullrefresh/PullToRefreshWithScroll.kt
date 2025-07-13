package br.com.dillmann.fireflycompanion.android.core.components.pullrefresh

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@ExperimentalMaterial3Api
fun PullToRefreshWithScroll(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scrollState = rememberScrollState()

    PullToRefresh(
        onRefresh = onRefresh,
        enabled = enabled,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth(),
        ) {
            content()
        }
    }
}

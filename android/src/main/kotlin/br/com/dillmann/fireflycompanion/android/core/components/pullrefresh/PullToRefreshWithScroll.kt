package br.com.dillmann.fireflycompanion.android.core.components.pullrefresh

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterial3Api
fun PullToRefreshWithScroll(
    modifier: Modifier = Modifier,
    onRefresh: suspend () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    PullToRefresh(
        onRefresh = onRefresh,
        enabled = enabled,
        modifier = modifier,
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(contentPadding),
            content = content,
        )
    }
}

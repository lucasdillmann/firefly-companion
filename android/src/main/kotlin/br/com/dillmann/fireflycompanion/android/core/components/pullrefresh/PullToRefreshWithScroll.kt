package br.com.dillmann.fireflycompanion.android.core.components.pullrefresh

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
    content: LazyListScope.() -> Unit,
) {
    PullToRefresh(
        onRefresh = onRefresh,
        enabled = enabled,
        modifier = modifier,
    ) {
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxWidth(),
            content = content,
        )
    }
}

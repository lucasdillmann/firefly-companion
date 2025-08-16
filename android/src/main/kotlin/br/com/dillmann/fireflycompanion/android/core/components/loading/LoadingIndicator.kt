package br.com.dillmann.fireflycompanion.android.core.components.loading

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier.size(24.dp),
) {
    CircularProgressIndicator(
        modifier = modifier,
        strokeWidth = 2.dp
    )
}

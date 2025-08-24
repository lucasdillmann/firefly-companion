package br.com.dillmann.fireflycompanion.android.core.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.components.animations.TransitionContainer
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibility

@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    targetState: Any? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Section(title, modifier) {
        Card(
            modifier = Modifier.minimumInteractiveComponentSize(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                TransitionContainer(
                    state = listOf(targetState, MoneyVisibility.state.value),
                ) {
                    content()
                }
            }
        }
    }
}

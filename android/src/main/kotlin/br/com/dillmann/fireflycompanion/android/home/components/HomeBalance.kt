package br.com.dillmann.fireflycompanion.android.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.summary.Summary
import java.math.BigDecimal

@Composable
fun HomeBalance(summary: Summary?) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            text = "Overview",
            style = MaterialTheme.typography.headlineSmall,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                DetailContent(
                    title = "Net worth",
                    summary = summary,
                    colorSchema = MaterialTheme.colorScheme.onPrimaryContainer,
                    valueStyle = MaterialTheme.typography.displaySmall,
                    labelStyle = MaterialTheme.typography.titleMedium,
                    valueProvider = { it.netWorth },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth()
                .align(Alignment.Start),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DetailBlock(
                title = "Earned",
                summary = summary,
                valueProvider = { it.earned },
            )
            DetailBlock(
                title = "Spent",
                summary = summary,
                valueProvider = { it.spent },
            )
            DetailBlock(
                title = "Left to spend",
                summary = summary,
                valueProvider = { it.leftToSpend },
            )
            DetailBlock(
                title = "Balance",
                summary = summary,
                valueProvider = { it.balance },
            )
        }
    }
}

@Composable
private fun DetailBlock(
    title: String,
    summary: Summary?,
    modifier: Modifier = Modifier,
    valueProvider: (Summary) -> BigDecimal?,
) {
    Card(
        modifier = modifier.minimumInteractiveComponentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            DetailContent(
                title = title,
                summary = summary,
                colorSchema = MaterialTheme.colorScheme.onSecondaryContainer,
                valueStyle = MaterialTheme.typography.bodyLarge,
                labelStyle = MaterialTheme.typography.labelLarge,
                valueProvider = valueProvider,
            )
        }
    }
}

@Composable
private fun DetailContent(
    title: String,
    summary: Summary?,
    colorSchema: Color,
    valueStyle: TextStyle,
    labelStyle: TextStyle,
    valueProvider: (Summary) -> BigDecimal?,
) {
    if (summary == null) {
        CircularProgressIndicator(
            modifier = Modifier.size(valueStyle.lineHeight.value.dp - 8.dp),
            color = colorSchema.copy(alpha = 0.5f),
            strokeWidth = 2.dp,
        )
        Spacer(modifier = Modifier.size(8.dp))
    } else {
        val value = valueProvider(summary)
        Text(
            text = formatValue(value, summary.currency),
            style = valueStyle.copy(fontWeight = FontWeight.SemiBold),
            color = colorSchema,
        )
    }

    Text(
        text = title,
        style = labelStyle,
        color = colorSchema.copy(alpha = 0.7f)
    )
}

private fun formatValue(value: BigDecimal?, currency: Currency?): String {
    if (value == null || currency == null)
        return "-"

    return currency.format(value)
}

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
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyText
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.theme.Colors
import br.com.dillmann.fireflycompanion.business.summary.Summary
import java.math.BigDecimal

@Composable
fun HomeOverview(summary: Summary?) {
    val scrollState = rememberScrollState()

    Section(
        title = i18n(R.string.overview),
        rightContent = {
            HomeTopActions()
        }
    ) {

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
                    title = i18n(R.string.net_worth),
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
                title = i18n(R.string.earned),
                summary = summary,
                tintColor = Colors.GREEN,
                valueProvider = { it.earned },
            )
            DetailBlock(
                title = i18n(R.string.spent),
                summary = summary,
                tintColor = Colors.RED,
                valueProvider = { it.spent?.abs() },
            )
            DetailBlock(
                title = i18n(R.string.left_to_spend),
                summary = summary,
                tintColor = Colors.BLUE,
                valueProvider = { it.leftToSpend },
            )
            DetailBlock(
                title = i18n(R.string.balance),
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
    tintColor: Color? = null,
    valueProvider: (Summary) -> BigDecimal?,
) {
    val baseColor = MaterialTheme.colorScheme.secondaryContainer
    val compositeColor = tintColor
        ?.copy(alpha = 0.25f)
        ?.compositeOver(baseColor)
        ?: baseColor

    Card(
        modifier = modifier
            .defaultMinSize(minWidth = 125.dp)
            .minimumInteractiveComponentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = compositeColor)
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
        MoneyText(
            value = valueProvider(summary) ?: BigDecimal.ZERO,
            currency = summary.currency,
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

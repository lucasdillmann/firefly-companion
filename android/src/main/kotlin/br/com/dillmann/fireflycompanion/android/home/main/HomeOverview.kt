package br.com.dillmann.fireflycompanion.android.home.main

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.animations.TransitionContainer
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyText
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.theme.AppColors
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
import br.com.dillmann.fireflycompanion.android.home.HomeTopActions
import br.com.dillmann.fireflycompanion.android.home.extensions.toDateRange
import br.com.dillmann.fireflycompanion.business.overview.model.SummaryOverview
import br.com.dillmann.fireflycompanion.business.overview.usecase.SummaryOverviewUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import java.math.BigDecimal

@Composable
fun HomeOverview() {
    val queue by persistent(ActionQueue())
    var summary by persistent(::fetchSummary)
    val scrollState = rememberScrollState()

    OnRefreshEvent("HomeOverview", HomeTabs.MAIN) {
        queue.add {
            summary = null
            summary = fetchSummary()
        }
    }

    Section(
        title = i18n(R.string.overview),
        rightContent = {
            HomeTopActions()
        }
    ) {
        HomePeriodSelector()

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                SummaryDetailsCards(
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
                tintColor = AppColors.Green,
                valueProvider = { it.earned },
            )
            DetailBlock(
                title = i18n(R.string.spent),
                summary = summary,
                tintColor = AppColors.Red,
                valueProvider = { it.spent?.abs() },
            )
            DetailBlock(
                title = i18n(R.string.reconciliation),
                summary = summary,
                tintColor = AppColors.Blue,
                valueProvider = { it.reconciliations },
            )
            DetailBlock(
                title = i18n(R.string.left_to_spend),
                summary = summary,
                tintColor = AppColors.Yellow,
                valueProvider = { it.leftToSpend },
            )
            DetailBlock(
                title = i18n(R.string.gross_balance),
                summary = summary,
                valueProvider = { it.balance },
            )
            DetailBlock(
                title = i18n(R.string.net_balance),
                summary = summary,
                valueProvider = { it.balance?.plus(it.reconciliations ?: BigDecimal.ZERO) },
            )
        }
    }
}

@Composable
private fun DetailBlock(
    title: String,
    summary: SummaryOverview?,
    modifier: Modifier = Modifier,
    tintColor: Color? = null,
    valueProvider: (SummaryOverview) -> BigDecimal?,
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
        colors = CardDefaults.cardColors(containerColor = compositeColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            SummaryDetailsCards(
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
private fun SummaryDetailsCards(
    title: String,
    summary: SummaryOverview?,
    colorSchema: Color,
    valueStyle: TextStyle,
    labelStyle: TextStyle,
    valueProvider: (SummaryOverview) -> BigDecimal?,
) {
    TransitionContainer(
        state = summary,
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
                baseColor = colorSchema,
            )
        }
    }

    Text(
        text = title,
        style = labelStyle,
        color = colorSchema.copy(alpha = 0.7f)
    )
}

private suspend fun fetchSummary(): SummaryOverview {
    val overviewUseCase = koin().get<SummaryOverviewUseCase>()
    val preferencesUseCase = koin().get<GetPreferencesUseCase>()
    val (startDate, endDate) = preferencesUseCase.getPreferences().homePeriod.toDateRange()
    return overviewUseCase.getSummary(startDate, endDate)
}

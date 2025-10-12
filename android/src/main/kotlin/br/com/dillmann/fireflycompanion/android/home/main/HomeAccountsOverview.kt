package br.com.dillmann.fireflycompanion.android.home.main

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.colorpool.ColorPool
import br.com.dillmann.fireflycompanion.android.core.components.loading.LoadingIndicator
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibility
import br.com.dillmann.fireflycompanion.android.core.components.section.SectionCard
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.theme.AppColors
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
import br.com.dillmann.fireflycompanion.android.home.extensions.toDateRange
import br.com.dillmann.fireflycompanion.business.account.AccountOverview
import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountOverviewUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.*

@Composable
fun HomeAccountsOverview() {
    val queue by persistent(ActionQueue())
    var overview by persistent(::fetchOverview)

    OnRefreshEvent("HomeAccountsOverview", HomeTabs.MAIN) {
        queue.add {
            overview = null
            overview = fetchOverview()
        }
    }

    SectionCard(
        modifier = Modifier.fillMaxWidth(),
        title = i18n(R.string.accounts_overview),
        targetState = overview,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            if (overview == null) {
                LoadingIndicator()
            } else {
                Graph(overview!!)
            }
        }
    }
}

@Composable
private fun Graph(overview: List<AccountOverview>) {
    val data by volatile {
        overview.mapIndexed { index, item ->
            val baseColor = ColorPool.indexed(index)
            Line(
                label = item.name,
                values = item.balanceHistory.values.map { it.toDouble() },
                curvedEdges = true,
                color = SolidColor(baseColor),
                firstGradientFillColor = baseColor.copy(alpha = 0.25f),
                secondGradientFillColor = Color.Transparent,
                gradientAnimationSpec = tween(0),
                strokeAnimationSpec = tween(0),
                gradientAnimationDelay = 0,
            )
        }
    }

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        data = data,
        zeroLineProperties = ZeroLineProperties(
            enabled = true,
            color = SolidColor(AppColors.Red),
        ),
        animationDelay = 0,
        animationMode = AnimationMode.Together(),
        gridProperties = GridProperties(
            enabled = false,
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            contentBuilder = {
                val currency = overview.first().currency
                MoneyVisibility.format(it.toBigDecimal(), currency)
            },
        ),
        popupProperties = PopupProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            contentBuilder = { index, _, value ->
                val item = overview[index]
                val value = MoneyVisibility.format(value.toBigDecimal(), item.currency)

                "${item.name}: $value"
            },
        ),
        labelHelperProperties = LabelHelperProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
        ),
    )
}

private suspend fun fetchOverview(): List<AccountOverview> {
    val overviewUseCase = get<GetAccountOverviewUseCase>()
    val preferencesUseCase = get<GetPreferencesUseCase>()
    val (startDate, endDate) = preferencesUseCase.getPreferences().homePeriod.toDateRange()
    return overviewUseCase.getOverview(startDate, endDate)
}

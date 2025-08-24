package br.com.dillmann.fireflycompanion.android.home.main

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.colorpool.ColorPool
import br.com.dillmann.fireflycompanion.android.core.components.contenthidden.ContentHiddenIcon
import br.com.dillmann.fireflycompanion.android.core.components.loading.LoadingIndicator
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibility
import br.com.dillmann.fireflycompanion.android.core.components.section.SectionCard
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
import br.com.dillmann.fireflycompanion.android.home.extensions.toDateRange
import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.currency.usecase.GetDefaultCurrencyUseCase
import br.com.dillmann.fireflycompanion.business.overview.model.ExpensesByCategoryOverview
import br.com.dillmann.fireflycompanion.business.overview.usecase.ExpensesByCategoryOverviewUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.*
import org.koin.java.KoinJavaComponent.getKoin

@Composable
fun HomeExpensesByCategory() {
    val queue by persistent(ActionQueue())
    val currency by persistent { getKoin().get<GetDefaultCurrencyUseCase>().getDefault() }
    var overview by persistent(::fetchOverview)

    OnRefreshEvent(HomeTabs.MAIN) {
        queue.add {
            overview = null
            overview = fetchOverview()
        }
    }

    SectionCard(
        modifier = Modifier.fillMaxWidth(),
        title = i18n(R.string.expenses_by_category),
        targetState = overview,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            if (overview == null || currency == null) {
                LoadingIndicator()
            } else if (overview!!.isEmpty()) {
                Text(
                    text = i18n(R.string.no_data_yet),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            } else {
                Graph(overview!!, currency!!)
            }
        }
    }
}

@Composable
private fun Graph(overview: List<ExpensesByCategoryOverview>, currency: Currency) {
    var data by volatile {
        overview.mapIndexed { index, item ->
            val color = ColorPool.indexed(index)
            Bars(
                label = item.name,
                values = listOf(
                    Bars.Data(
                        label = item.name,
                        value = item.amount.abs().toDouble(),
                        color = Brush.verticalGradient(
                            listOf(
                                color,
                                color.copy(alpha = 0.5f),
                            )
                        ),
                    )
                ),
            )
        }
    }

    ColumnChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        data = data,
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            spacing = 3.dp,
            thickness = 20.dp,
        ),
        animationDelay = 0,
        animationSpec = tween(0),
        animationMode = AnimationMode.Together(),
        gridProperties = GridProperties(enabled = false),
        labelProperties = LabelProperties(enabled = false),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            contentBuilder = {
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
                val value = MoneyVisibility.format(value.toBigDecimal(), currency)

                "${item.name}: $value"
            },
        ),
        labelHelperProperties = LabelHelperProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
        )
    )
}

private suspend fun fetchOverview(): List<ExpensesByCategoryOverview> {
    val (startDate, endDate) = getKoin().get<GetPreferencesUseCase>().getPreferences().homePeriod.toDateRange()
    return getKoin()
        .get<ExpensesByCategoryOverviewUseCase>()
        .getExpensesByCategory(startDate, endDate)
        .sortedBy { it.amount }
}

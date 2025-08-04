package br.com.dillmann.fireflycompanion.android.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import br.com.dillmann.fireflycompanion.android.core.activity.persistent
import br.com.dillmann.fireflycompanion.android.core.activity.volatile
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibility
import br.com.dillmann.fireflycompanion.android.core.components.section.SectionCard
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.theme.Colors
import br.com.dillmann.fireflycompanion.business.account.AccountOverview
import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountOverviewUseCase
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.*
import org.koin.mp.KoinPlatform.getKoin
import kotlin.random.Random

@Composable
fun HomeAccountsOverview() {
    val useCase = getKoin().get<GetAccountOverviewUseCase>()
    val monetaryValuesVisible by MoneyVisibility.state
    var overview by persistent { useCase.getOverview() }

    OnRefreshEvent {
        overview = null
        overview = useCase.getOverview()
    }

    SectionCard(
        modifier = Modifier.fillMaxWidth(),
        title = i18n(R.string.accounts_overview),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            if (overview == null) {
                LoadingIndicator()
            } else if (!monetaryValuesVisible) {
                ContentHiddenIcon()
            } else {
                Graph(overview!!)
            }
        }
    }
}

@Composable
private fun ContentHiddenIcon() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            contentDescription = "",
            imageVector = Icons.Filled.VisibilityOff,
        )
    }
}

@Composable
private fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(24.dp),
        strokeWidth = 2.dp
    )
}

@Composable
private fun Graph(overview: List<AccountOverview>) {
    val data by volatile {
        overview.map {
            val baseColor = randomColor()
            Line(
                label = it.name,
                values = it.balanceHistory.values.map { it.toDouble() },
                curvedEdges = true,
                color = SolidColor(baseColor),
                firstGradientFillColor = baseColor.copy(alpha = 0.25f),
                secondGradientFillColor = Color.Transparent,
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
            color = SolidColor(Colors.RED),
        ),
        animationMode = AnimationMode.Together(
            delayBuilder = { it.toLong() * 100 },
        ),
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
                currency.format(it.toBigDecimal())
            },
        ),
        popupProperties = PopupProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            contentBuilder = { index, _, value ->
                val item = overview[index]
                val value = item.currency.format(value.toBigDecimal())

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

private fun randomColor() =
    Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
    )

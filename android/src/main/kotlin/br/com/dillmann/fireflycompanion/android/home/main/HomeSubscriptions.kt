package br.com.dillmann.fireflycompanion.android.home.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.animations.TransitionContainer
import br.com.dillmann.fireflycompanion.android.core.components.loading.LoadingIndicator
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyText
import br.com.dillmann.fireflycompanion.android.core.components.section.SectionCard
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.theme.AppColors
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
import br.com.dillmann.fireflycompanion.android.home.extensions.toDateRange
import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.subscription.Subscription
import br.com.dillmann.fireflycompanion.business.subscription.usecase.SubscriptionOverviewUseCase
import br.com.dillmann.fireflycompanion.core.pagination.fetchAllPages
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun HomeSubscriptions() {
    val actionQueue by persistent(ActionQueue())
    val expanded = persistent(false)
    var subscriptions by persistent(::fetchSubscriptions)

    OnRefreshEvent("HomeSubscriptions", HomeTabs.MAIN) {
        actionQueue.add {
            subscriptions = null
            subscriptions = fetchSubscriptions()
        }
    }

    SectionCard(
        title = i18n(R.string.subscriptions),
        targetState = subscriptions,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            if (subscriptions == null) {
                LoadingIndicator()
            } else {
                SubscriptionList(subscriptions!!, expanded)
            }
        }
    }
}

@Composable
private fun SubscriptionList(subscriptions: List<Subscription>, expanded: MutableState<Boolean>) {
    if (subscriptions.isEmpty()) {
        Text(
            text = i18n(R.string.no_data_yet),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Overview(subscriptions, subscriptions.first().currency)

        TransitionContainer(state = expanded.value) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (expanded.value) {
                    subscriptions.forEachIndexed { index, subscription ->
                        SubscriptionDetails(subscription)

                        if (index < subscriptions.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 0.dp),
                                thickness = 0.2.dp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                ExpandCollapseButton(expanded)
            }
        }
    }
}

@Composable
private fun SubscriptionDetails(subscription: Subscription) {
    val (date, amount) = subscription.payment ?: return
    val (statusDescription, statusColor) =
        if (subscription.payment!!.pending) R.string.to_pay_at to AppColors.Yellow
        else R.string.paid_at to AppColors.Green
    val style =
        MaterialTheme.typography.bodyMedium.copy(color = statusColor)
    val statusText =
        i18n(statusDescription) + " " +
            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(date)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = subscription.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MoneyText(
                    value = amount.abs(),
                    currency = subscription.currency,
                    dynamicColors = false,
                    style = style,
                    baseColor = statusColor,
                )

                Text(
                    text = " $statusText",
                    style = style,
                )
            }
        }
    }
}

@Composable
private fun Overview(subscriptions: List<Subscription>, currency: Currency) {
    val pendingSubscriptions = subscriptions.filter { it.payment?.pending == true }
    val paidSubscriptions = subscriptions.filter { it.payment?.pending == false }

    val pendingAmount = pendingSubscriptions.sumOf { it.payment!!.amount }.toFloat()
    val paidAmount = paidSubscriptions.sumOf { it.payment!!.amount }.toFloat()
    val totalAmount = pendingAmount + paidAmount

    val paidPercentage = if (totalAmount > 0) (paidAmount / totalAmount * 100) else 0f
    val pendingPercentage = if (totalAmount > 0) (pendingAmount / totalAmount * 100) else 0f

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OverviewText(
                text =
                    if (pendingAmount == 0f) R.string.no_subscriptions_left_to_pay
                    else R.string.yet_to_be_paid,
                currency = currency,
                amount = pendingAmount.takeIf { it > 0f }?.toBigDecimal(),
                style = MaterialTheme.typography.bodyLarge,
            )

            OverviewText(
                text =
                    if (paidAmount == 0f) R.string.nothing_paid_so_far
                    else R.string.paid_so_far,
                currency = currency,
                amount = paidAmount.takeIf { it > 0f }?.toBigDecimal(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        if (totalAmount == 0f)
            return

        Box(
            modifier = Modifier.size(60.dp),
            contentAlignment = Alignment.Center,
        ) {
            PieChart(
                modifier = Modifier.size(50.dp),
                style = Pie.Style.Stroke(width = 10.dp),
                data = listOf(
                    Pie(
                        label = i18n(R.string.paid_at),
                        data = paidPercentage.toDouble(),
                        color = AppColors.Green,
                    ),
                    Pie(
                        label = i18n(R.string.to_pay_at),
                        data = pendingPercentage.toDouble(),
                        color = AppColors.Yellow,
                    ),
                ),
            )
        }
    }
}

@Composable
private fun OverviewText(
    text: Int,
    amount: BigDecimal?,
    currency: Currency,
    style: TextStyle,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (amount != null) {
            MoneyText(
                value = amount,
                currency = currency,
                style = style,
            )
        }

        Text(
            text = i18n(text),
            style = style,
        )
    }
}

@Composable
private fun ExpandCollapseButton(expanded: MutableState<Boolean>) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(
            onClick = { expanded.value = !expanded.value },
            modifier = Modifier.size(26.dp).fillMaxWidth(),
        ) {
            Icon(
                contentDescription = "",
                modifier = Modifier.size(20.dp),
                imageVector =
                    if (expanded.value) Icons.Filled.ExpandLess
                    else Icons.Filled.ExpandMore,
            )
        }
    }
}

private suspend fun fetchSubscriptions(): List<Subscription> {
    val useCase = get<SubscriptionOverviewUseCase>()
    val preferencesUseCase = get<GetPreferencesUseCase>()
    val (startDate, endDate) = preferencesUseCase.getPreferences().homePeriod.toDateRange()
    return fetchAllPages { useCase.subscriptionsOverview(it, startDate, endDate) }
        .filter { it.active }
        .sortedBy { it.name }
        .sortedBy { it.payment?.pending == false }
}

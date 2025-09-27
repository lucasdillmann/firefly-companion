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
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.theme.AppColors
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
import br.com.dillmann.fireflycompanion.android.home.extensions.toDateRange
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.subscription.Subscription
import br.com.dillmann.fireflycompanion.business.subscription.usecase.SubscriptionOverviewUseCase
import br.com.dillmann.fireflycompanion.core.pagination.fetchAllPages
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.mp.KoinPlatform
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val COLLAPSED_LIST_SIZE: Int = 5

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

    val targetList =
        if (expanded.value) subscriptions
        else subscriptions.take(COLLAPSED_LIST_SIZE)

    TransitionContainer(state = expanded.value) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            targetList.forEachIndexed { index, subscription ->
                SubscriptionDetails(subscription)

                if (index < targetList.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 0.dp),
                        thickness = 0.2.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (subscriptions.size > COLLAPSED_LIST_SIZE) {
                ExpandCollapseButton(expanded)
            }
        }
    }
}

@Composable
private fun SubscriptionDetails(subscription: Subscription) {
    val (date, amount) = subscription.lastPayment
        ?: subscription.expectedPayment
        ?: return
    val (statusDescription, statusColor) =
        if (subscription.lastPayment == null) R.string.to_pay_at to AppColors.Yellow
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
private fun ExpandCollapseButton(expanded: MutableState<Boolean>) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(
            onClick = { expanded.value = !expanded.value },
            modifier = Modifier.size(32.dp),
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
    val useCase = getKoin().get<SubscriptionOverviewUseCase>()
    val preferencesUseCase = KoinPlatform.getKoin().get<GetPreferencesUseCase>()
    val (startDate, endDate) = preferencesUseCase.getPreferences().homePeriod.toDateRange()
    return fetchAllPages { useCase.subscriptionsOverview(it, startDate, endDate) }
        .filter { it.active }
        .sortedBy { it.name }
        .sortedBy { it.lastPayment != null }
}

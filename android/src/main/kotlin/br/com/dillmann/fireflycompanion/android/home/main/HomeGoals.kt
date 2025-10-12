package br.com.dillmann.fireflycompanion.android.home.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.loading.LoadingIndicator
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibility
import br.com.dillmann.fireflycompanion.android.core.components.section.SectionCard
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
import br.com.dillmann.fireflycompanion.business.goal.Goal
import br.com.dillmann.fireflycompanion.business.goal.usecase.ListGoalsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.fetchAllPages
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

@Composable
fun HomeGoals() {
    val actionQueue by persistent(ActionQueue())
    var goals by persistent(::fetchGoals)

    OnRefreshEvent("HomeGoals", HomeTabs.MAIN) {
        actionQueue.add {
            goals = null
            goals = fetchGoals()
        }
    }

    SectionCard(
        title = i18n(R.string.goals),
        targetState = goals,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            if (goals == null) {
                LoadingIndicator()
            } else {
                GoalsList(goals!!)
            }
        }
    }
}

@Composable
private fun GoalsList(goals: List<Goal>) {
    if (goals.isEmpty()) {
        Text(
            text = i18n(R.string.no_data_yet),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        goals.forEachIndexed { index, goal ->
            GoalDetails(goal)

            if (index < goals.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 0.dp),
                    thickness = 0.2.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun GoalDetails(goal: Goal) {
    Column {
        GoalText(
            text = goal.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 6.dp),
        )
        DateProgress(goal)
        AmountProgress(goal)
    }
}

@Composable
private fun DateProgress(goal: Goal) {
    val start = goal.startDate.toLocalDate()
    val end = goal.targetDate.toLocalDate()
    val remainingDays = ChronoUnit.DAYS.between(start, end)
    val to = i18n(R.string.to)
    val daysLeft = i18n(R.string.days_left)
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    GoalText(
        text = "${formatter.format(start)} $to ${formatter.format(end)} ($remainingDays $daysLeft)",
        icon = Icons.Filled.CalendarMonth,
    )
}

@Composable
private fun AmountProgress(goal: Goal) {
    val currentFormatted = MoneyVisibility.format(goal.currentAmount, goal.currency)
    val targetFormatted = MoneyVisibility.format(goal.targetAmount, goal.currency)
    val percentage = completionPercentage(goal)
    val separator = i18n(R.string.out_of)
    val achieved = i18n(R.string.achieved)

    GoalText(
        text = "$currentFormatted $separator $targetFormatted",
        icon = Icons.Filled.AttachMoney,
    )
    GoalText(
        text = "$percentage% $achieved",
        icon = Icons.Filled.Percent,
    )
}

@Composable
private fun GoalText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    icon: ImageVector? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = text,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 4.dp, end = 6.dp),
            )
        }

        Text(
            text = text,
            style = style.copy(
                lineHeight = style.fontSize,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both,
                    mode = LineHeightStyle.Mode.Fixed,
                )
            ),
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun completionPercentage(goal: Goal): BigDecimal {
    if (goal.targetAmount.compareTo(BigDecimal.ZERO) <= 0)
        return BigDecimal.ZERO

    val precision = 2
    val roundingMode = RoundingMode.HALF_EVEN
    val current = goal.currentAmount
    val target = goal.targetAmount

    val rawValue = current.divide(target, precision, roundingMode) * BigDecimal(100)
    return rawValue.setScale(precision, roundingMode)
}

private suspend fun fetchGoals(): List<Goal> {
    val useCase = get<ListGoalsUseCase>()
    return fetchAllPages { useCase.listGoals(it) }.filter { it.active }
}

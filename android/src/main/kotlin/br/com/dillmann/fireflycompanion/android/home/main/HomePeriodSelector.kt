package br.com.dillmann.fireflycompanion.android.home.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.selectionrow.SelectionRow
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.android.core.refresh.RefreshDispatcher
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
import br.com.dillmann.fireflycompanion.business.preferences.Preferences.HomePeriod
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase

@Composable
fun HomePeriodSelector() {
    SelectionRow(
        options = HomePeriod.entries.toList(),
        textRenderer = ::translate,
        onOptionSelected = ::saveSelection,
        initialSelection = getPreferences().homePeriod,
        pinSelectionToStart = true,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}

private fun translate(option: HomePeriod) =
    when (option) {
        HomePeriod.LAST_WEEK -> R.string.last_week
        HomePeriod.LAST_MONTH -> R.string.last_month
        HomePeriod.LAST_YEAR -> R.string.last_year
        HomePeriod.CURRENT_WEEK -> R.string.current_week
        HomePeriod.CURRENT_MONTH -> R.string.current_month
        HomePeriod.CURRENT_YEAR -> R.string.current_year
        HomePeriod.WEEK_SO_FAR -> R.string.week_so_far
        HomePeriod.MONTH_SO_FAR -> R.string.month_so_far
        HomePeriod.YEAR_SO_FAR -> R.string.year_so_far
        HomePeriod.ALL_TIME -> R.string.all_time
    }.let {
        i18n(it)
    }

private fun saveSelection(newSelection: HomePeriod) {
    async {
        val updated = getPreferences().copy(homePeriod = newSelection)
        get<SavePreferencesUseCase>().savePreferences(updated)
        RefreshDispatcher.notify(HomeTabs.MAIN)
    }
}

private fun getPreferences() =
    async { get<GetPreferencesUseCase>().getPreferences() }.get()

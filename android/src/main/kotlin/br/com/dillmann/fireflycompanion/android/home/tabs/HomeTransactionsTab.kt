package br.com.dillmann.fireflycompanion.android.home.tabs

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.components.transactions.TransactionList
import br.com.dillmann.fireflycompanion.android.core.components.transactions.TransactionListContext
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.home.components.HomeTopActions
import br.com.dillmann.fireflycompanion.android.home.components.HomeTransactionsSearchField
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeTransactionsTab(
    modifier: Modifier = Modifier,
) {
    val listUseCase = koin().get<ListTransactionsUseCase>()
    val searchUseCase = koin().get<SearchTransactionsUseCase>()
    var searchTerms by persistent("")
    var listContext: TransactionListContext? = null

    Section(
        title = i18n(R.string.tab_transactions),
        rightContent = {
            HomeTopActions()
        }
    ) {
        listContext = TransactionList(
            showAccountNameOnReconciliation = true,
            modifier = modifier,
            header = { context ->
                HomeTransactionsSearchField(
                    searchTerms = searchTerms,
                    enabled = !context.isLoading(),
                    onChange = { searchTerms = it },
                    refresh = context::refresh,
                )
            },
            transactionsProvider = {
                if (searchTerms.isBlank()) listUseCase.list(it)
                else searchUseCase.search(it, searchTerms)
            }
        )
    }

    OnRefreshEvent {
        listContext!!.refresh()
    }

    LaunchedEffect(Unit) {
        if (!listContext!!.isLoading() && !listContext.containsData())
            listContext.refresh()
    }

    BackHandler(enabled = searchTerms.isNotBlank()) {
        searchTerms = ""
        listContext!!.refresh()
    }
}
